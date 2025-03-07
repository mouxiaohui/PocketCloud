package com.xiaohui.pocket.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohui.pocket.common.exception.BusinessException;
import com.xiaohui.pocket.common.result.ResultCode;
import com.xiaohui.pocket.common.utils.FileUtils;
import com.xiaohui.pocket.storage.engine.core.StorageEngine;
import com.xiaohui.pocket.storage.engine.dto.StoreFileDto;
import com.xiaohui.pocket.system.converter.FileConverter;
import com.xiaohui.pocket.system.mapper.RealFileMapper;
import com.xiaohui.pocket.system.model.dto.FileSaveDto;
import com.xiaohui.pocket.system.model.dto.QueryRealFileListDto;
import com.xiaohui.pocket.system.model.entity.RealFile;
import com.xiaohui.pocket.system.service.RealFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaohui
 * @since 2025/3/4
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RealFileServiceImpl extends ServiceImpl<RealFileMapper, RealFile> implements RealFileService {

    private final StorageEngine storageEngine;

    private final FileConverter fileConverter;

    /**
     * 根据条件查询用户的实际文件列表
     *
     * @param queryRealFileListDto 查询条件
     * @return 实际文件列表
     */
    @Override
    public List<RealFile> getFileList(QueryRealFileListDto queryRealFileListDto) {
        Long userId = queryRealFileListDto.getUserId();
        String identifier = queryRealFileListDto.getIdentifier();
        LambdaQueryWrapper<RealFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(userId), RealFile::getCreateUser, userId);
        queryWrapper.eq(StringUtils.isNotBlank(identifier), RealFile::getIdentifier, identifier);
        return list(queryWrapper);
    }

    /**
     * 保存文件
     *
     * @param fileSaveDto 文件保存实体
     * @return 物理文件保存的信息
     */
    @Override
    public RealFile save(FileSaveDto fileSaveDto) {
        try {
            StoreFileDto storeFileDto = StoreFileDto.builder()
                    .inputStream(fileSaveDto.getFile().getInputStream())
                    .filename(fileSaveDto.getFilename())
                    .totalSize(fileSaveDto.getTotalSize())
                    .build();

            // 保存物理文件
            String realPath = storageEngine.store(storeFileDto);

            // 保存物理文件记录
            RealFile realFile = fileConverter.toRealFileEntity(fileSaveDto);
            realFile.setRealPath(realPath);
            realFile.setFilePreviewContentType(FileUtils.getContentType(realPath));
            if (!save(realFile)) {
                // 如果保存记录失败，删除物理文件
                storageEngine.delete(List.of(realPath));
            }

            return realFile;
        } catch (IOException e) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED);
        }
    }
}
