package com.xiaohui.pocket.system.converter;

import com.xiaohui.pocket.system.model.dto.*;
import com.xiaohui.pocket.system.model.entity.RealFile;
import com.xiaohui.pocket.system.model.entity.UserFile;
import com.xiaohui.pocket.system.model.form.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 用户文件信息转换器
 *
 * @author xiaohui
 * @since 2025/2/28
 */
@Mapper(componentModel = "spring")
public interface FileConverter {

    @Mapping(target = "filename", source = "folderName")
    UserFile toUserFileEntity(CreateFolderDto createFolderDto);

    @Mapping(target = "folderFlag", expression = "java(com.xiaohui.pocket.system.enums.FolderFlagEnum.NO.getCode())")
    @Mapping(target = "createUser", source = "userId")
    @Mapping(target = "updateUser", source = "userId")
    UserFile toUserFileEntity(FileUploadDto fileUploadDto);

    @Mapping(target = "filename", source = "newFilename")
    @Mapping(target = "updateUser", source = "userId")
    UserFile toUserFileEntity(UpdateFilenameDto updateFilenameDto);

    @Mapping(target = "folderFlag", expression = "java(com.xiaohui.pocket.system.enums.FolderFlagEnum.NO.getCode())")
    @Mapping(target = "createUser", source = "userId")
    @Mapping(target = "updateUser", source = "userId")
    UserFile toUserFileEntity(SecUploadFileDto secUploadFileDto);

    @Mapping(target = "fileSize", expression = "java(String.valueOf(fileSaveDto.getTotalSize()))")
    @Mapping(target = "fileSizeDesc", expression = "java(com.xiaohui.pocket.common.utils.FileUtils.byteCountToDisplaySize(fileSaveDto.getTotalSize()))")
    @Mapping(target = "fileSuffix", expression = "java(com.xiaohui.pocket.common.utils.FileUtils.getFileSuffix(fileSaveDto.getFilename()))")
    @Mapping(target = "createUser", source = "userId")
    RealFile toRealFileEntity(FileSaveDto fileSaveDto);

    @Mapping(target = "parentId", expression = "java(com.xiaohui.pocket.common.utils.IdUtil.decrypt(createFolderForm.getParentId()))")
    @Mapping(target = "userId", expression = "java(com.xiaohui.pocket.core.context.BaseContext.getUserId())")
    CreateFolderDto toCreateFolderDto(CreateFolderForm createFolderForm);

    @Mapping(target = "userId", expression = "java(com.xiaohui.pocket.core.context.BaseContext.getUserId())")
    @Mapping(target = "fileId", expression = "java(com.xiaohui.pocket.common.utils.IdUtil.decrypt(updateFilenameForm.getFileId()))")
    UpdateFilenameDto toUpdateFilenameDto(UpdateFilenameForm updateFilenameForm);

    @Mapping(target = "parentId", expression = "java(com.xiaohui.pocket.common.utils.IdUtil.decrypt(fileUploadForm.getParentId()))")
    @Mapping(target = "userId", expression = "java(com.xiaohui.pocket.core.context.BaseContext.getUserId())")
    FileUploadDto toUploadDto(FileUploadForm fileUploadForm);

    @Mapping(target = "userId", expression = "java(com.xiaohui.pocket.core.context.BaseContext.getUserId())")
    SecUploadFileDto toSecUploadFileDto(SecUploadFileForm secUploadFileForm);

    @Mapping(target = "userId", expression = "java(com.xiaohui.pocket.core.context.BaseContext.getUserId())")
    FileChunkUploadDto toChunkUploadDto(FileChunkUploadForm fileChunkUploadForm);

    FileChunkSaveDto toChunkSaveDto(FileChunkUploadDto fileChunkUploadDto);

    QueryRealFileListDto toQueryRealFileListDto(SecUploadFileDto secUploadFileDto);

    FileSaveDto toSaveDto(FileUploadDto fileUploadDto);

}
