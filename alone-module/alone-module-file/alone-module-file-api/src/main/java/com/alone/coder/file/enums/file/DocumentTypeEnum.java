package com.alone.coder.file.enums.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文档类型
 *
 * @author AgoniMou
 * @since 2023/4/15 17:41
 */
@Getter
@AllArgsConstructor
public enum DocumentTypeEnum {

    /**
     * 图片
     */
    IMAGE("IMAGE", Arrays.asList("png", "jpg", "gif", "tif", "tiff", "bmp", "aix", "ico", "ps", "eps", "svg", "psd", "ai", "jpeg")),

    /**
     * 音频
     */
    AUDIO("AUDIO", Arrays.asList("mp3", "WMA", "AAC", "ogg", "wav")),

    /**
     * 视频
     */
    VIDEO("VIDEO", Arrays.asList("mp4", "mov", "avi", "wmv", "flv", "mpeg", "mkv", "vob")),

    /**
     * 文档
     */
    DOCUMENT("DOCUMENT", Arrays.asList("docx", "docm", "odt", "doc", "dot", "dotm", "dotx", "rtf", "wps")),

    /**
     * PPT
     */
    PPT("PPT", Arrays.asList("pptx", "ppsx", "odp", "pot", "potm", "potx", "pps", "ppsm", "ppt", "pptm", "dps")),

    /**
     * EXCEL
     */
    EXCEL("EXCEL", Arrays.asList("xlsx", "xlsb", "xlsm", "xls", "ods", "csv")),

    /**
     * PDF
     */
    PDF("PDF", Collections.singletonList("pdf")),

    /**
     * XMIND 思维导图
     */
    XMIND("XMIND", Collections.singletonList("xmind")),

    /**
     * CAD 文件
     */
    CAD("CAD", Arrays.asList("dwg", "dxf", "dwf", "iges", "igs", "dwt", "dng", "ifc", "dwfx", "stl", "cf2", "plt")),

    /**
     * 3D绘制文件
     */
    ONLINE3D("ONLINE3D", Arrays.asList("obj", "3ds", "stl", "ply", "off", "3dm", "fbx", "dae", "wrl", "3mf", "ifc", "glb", "o3dv", "gltf", "stp", "bim", "fcstd", "step", "iges", "brep")),

    /**
     * 其他
     */
    OTHER("OTHER", Collections.emptyList());


    /**
     * 文档类型
     */
    private final String type;

    /**
     * 拓展名
     */
    private final List<String> extensions;

    public static DocumentTypeEnum matching(String extension) {
        for (DocumentTypeEnum type : DocumentTypeEnum.values()) {
            if (type.getExtensions().contains(extension)) {
                return type;
            }
        }
        return DocumentTypeEnum.OTHER;
    }
}
