package com.xiaohui.pocket.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohui.pocket.common.exception.BusinessException;
import com.xiaohui.pocket.common.result.ResultCode;
import com.xiaohui.pocket.system.constants.FileConstants;
import com.xiaohui.pocket.system.converter.FileConverter;
import com.xiaohui.pocket.system.enums.FileTypeEnum;
import com.xiaohui.pocket.system.enums.FolderFlagEnum;
import com.xiaohui.pocket.system.enums.MergeFlagEnum;
import com.xiaohui.pocket.system.mapper.UserFileMapper;
import com.xiaohui.pocket.system.model.dto.*;
import com.xiaohui.pocket.system.model.entity.RealFile;
import com.xiaohui.pocket.system.model.entity.UserFile;
import com.xiaohui.pocket.system.model.vo.FileChunkUploadVO;
import com.xiaohui.pocket.system.model.vo.UserFileVO;
import com.xiaohui.pocket.system.service.FileChunkService;
import com.xiaohui.pocket.system.service.RealFileService;
import com.xiaohui.pocket.system.service.UserFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaohui
 * @since 2025/2/28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    private final FileConverter fileConverter;

    private final RealFileService realFileService;

    private final FileChunkService fileChunkService;

    /**
     * 创建文件夹
     *
     * @param createFolderDto 文件夹信息
     * @return 文件夹ID
     */
    @Override
    public Long createFolder(CreateFolderDto createFolderDto) {
        UserFile userFile = fileConverter.toUserFileEntity(createFolderDto);
        userFile.setFolderFlag(FolderFlagEnum.YES.getCode());
        userFile.setCreateUser(createFolderDto.getUserId());
        userFile.setUpdateUser(createFolderDto.getUserId());

        if (!save(userFile)) {
            throw new BusinessException(ResultCode.CREATE_FOLDER_FAILED);
        }

        return userFile.getId();
    }

    /**
     * 更新文件名称
     *
     * @param updateFilenameDto 文件名称更新参数
     */
    @Override
    public void updateFilename(UpdateFilenameDto updateFilenameDto) {
        UserFile userFile = fileConverter.toUserFileEntity(updateFilenameDto);

        LambdaUpdateWrapper<UserFile> updateWrapper = new LambdaUpdateWrapper<UserFile>()
                .eq(UserFile::getId, updateFilenameDto.getFileId())
                .eq(UserFile::getUserId, updateFilenameDto.getUserId());

        if (!update(userFile, updateWrapper)) {
            throw new BusinessException(ResultCode.UPDATE_FILENAME_FAILED);
        }
    }

    /**
     * 查询用户的根文件夹信息
     *
     * @param userId 用户ID
     * @return 用户根文件夹信息
     */
    @Override
    public UserFile getUserRootFolder(Long userId) {
        LambdaQueryWrapper<UserFile> queryWrapper = new LambdaQueryWrapper<UserFile>()
                .eq(UserFile::getUserId, userId)
                .eq(UserFile::getParentId, FileConstants.TOP_PARENT_ID)
                .eq(UserFile::getFolderFlag, FolderFlagEnum.YES.getCode());
        return getOne(queryWrapper);
    }

    /**
     * 查询文件信息列表
     *
     * @param queryFileListDto 查询文件信息列表参数
     * @return 文件信息列表
     */
    @Override
    public List<UserFileVO> getFileList(QueryFileListDto queryFileListDto) {
        return this.baseMapper.selectFileList(queryFileListDto);
    }

    /**
     * 单文件上传
     *
     * @param fileUploadDto 文件上传参数
     */
    @Override
    public void upload(FileUploadDto fileUploadDto) {
        // 保存物理文件
        FileSaveDto fileSaveDto = fileConverter.toSaveDto(fileUploadDto);
        RealFile realFile = realFileService.save(fileSaveDto);

        // 保存用户文件记录
        UserFile userFile = fileConverter.toUserFileEntity(fileUploadDto);
        userFile.setRealFileId(realFile.getId());
        userFile.setFileSizeDesc(realFile.getFileSizeDesc());
        userFile.setFileType(FileTypeEnum.getFileTypeCode(realFile.getFileSuffix()));
        if (!save(userFile)) {
            throw new BusinessException(ResultCode.SAVE_FILE_INFO_FAILED);
        }
    }

    /**
     * 文件分片上传
     * <p>
     * 1、上传实体文件
     * 2、保存分片文件记录
     * 3、校验是否全部分片上传完成
     *
     * @param fileChunkUploadDto 文件分片上传参数
     * @return 文件分片上传结果
     */
    @Override
    public FileChunkUploadVO chunkUpload(FileChunkUploadDto fileChunkUploadDto) {
        FileChunkSaveDto fileChunkSaveDto = fileConverter.toChunkSaveDto(fileChunkUploadDto);
        MergeFlagEnum mergeFlagEnum = fileChunkService.saveChunkFile(fileChunkSaveDto);
        return FileChunkUploadVO.builder().mergeFlag(mergeFlagEnum.getCode()).build();
    }

    /**
     * 文件秒传功能
     *
     * @param secUploadFileDto 文件秒传参数
     * @return 文件秒传结果
     */
    @Override
    public boolean secUpload(SecUploadFileDto secUploadFileDto) {
        QueryRealFileListDto queryRealFileListDto = fileConverter.toQueryRealFileListDto(secUploadFileDto);
        List<RealFile> fileList = realFileService.getFileList(queryRealFileListDto);

        if (CollectionUtils.isNotEmpty(fileList)) {
            RealFile realFile = fileList.get(0);
            UserFile userFile = fileConverter.toUserFileEntity(secUploadFileDto);
            userFile.setRealFileId(realFile.getId());
            userFile.setFileSizeDesc(realFile.getFileSizeDesc());
            userFile.setFileType(FileTypeEnum.getFileTypeCode(secUploadFileDto.getFilename()));
            return save(userFile);
        }

        return false;
    }

}
