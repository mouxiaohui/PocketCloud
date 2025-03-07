package com.xiaohui.pocket.system.enums;

import com.xiaohui.pocket.common.exception.BusinessException;
import com.xiaohui.pocket.common.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 文件类型枚举类
 * 文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv）
 *
 * @author xiaohui
 * @since 2025/3/4
 */
@AllArgsConstructor
@Getter
public enum FileTypeEnum {

    NORMAL_FILE(1, "NORMAL_FILE", 1, fileSuffix -> true),

    ARCHIVE_FILE(2, "ARCHIVE_FILE", 2, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".rar", ".zip", ".cab", ".iso", ".jar", ".ace", ".7z", ".tar", ".gz", ".arj", ".lah", ".uue", ".bz2", ".z", ".war");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    EXCEL_FILE(3, "EXCEL", 3, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".xlsx", ".xls");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    WORD_FILE(4, "WORD_FILE", 4, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".docx", ".doc");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    PDF_FILE(5, "PDF_FILE", 5, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".pdf");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    TXT_FILE(6, "TXT_FILE", 6, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".txt");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    IMAGE_FILE(7, "IMAGE_FILE", 7, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".bmp", ".gif", ".png", ".ico", ".eps", ".psd", ".tga", ".tiff", ".jpg", ".jpeg");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    AUDIO_FILE(8, "AUDIO_FILE", 8, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".mp3", ".mkv", ".mpg", ".rm", ".wma");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    VIDEO_FILE(9, "VIDEO_FILE", 9, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".avi", ".3gp", ".mp4", ".flv", ".rmvb", ".mov");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    POWER_POINT_FILE(10, "POWER_POINT_FILE", 10, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".ppt", ".pptx");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    SOURCE_CODE_FILE(11, "SOURCE_CODE_FILE", 11, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".java", ".obj", ".h", ".c", ".html", ".net", ".php", ".css", ".js", ".ftl", ".jsp", ".asp");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),

    CSV_FILE(12, "CSV_FILE", 12, fileSuffix -> {
        List<String> matchFileSuffixes = List.of(".csv");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    });

    /**
     * 文件类型的code
     */
    private final Integer code;

    /**
     * 文件类型描述
     */
    private final String desc;

    /**
     * 排序字段
     * 按照降序顺序排序
     */
    private final Integer order;

    /**
     * 文件类型匹配器
     */
    private final Predicate<String> tester;

    /**
     * 根据文件名称的后缀获取对应的文件类型映射code
     *
     * @param fileSuffix 文件后缀
     * @return 文件类型code
     */
    public static Integer getFileTypeCode(String fileSuffix) {
        Optional<FileTypeEnum> result = Arrays.stream(values())
                .sorted(Comparator.comparingInt(FileTypeEnum::getOrder).reversed())
                .filter(value -> value.getTester().test(fileSuffix))
                .findFirst();
        if (result.isPresent()) {
            return result.get().getCode();
        }
        throw new BusinessException(ResultCode.GET_FILE_TYPE_FAILED);
    }

}
