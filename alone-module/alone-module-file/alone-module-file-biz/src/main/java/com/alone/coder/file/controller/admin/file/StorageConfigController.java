package com.alone.coder.file.controller.admin.file;

import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigCreateReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigPageReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigRespVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigUpdateReqVO;
import com.alone.coder.file.convert.file.StorageConfigConvert;
import com.alone.coder.file.dal.dataobject.file.StorageConfig;
import com.alone.coder.file.service.file.StorageConfigService;
import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.common.pojo.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author AgoniMou
 */
@Tag(name = "管理后台 - 文件配置")
@RestController
@RequestMapping("/infra/storage/config")
@Validated
public class StorageConfigController {

    @Resource
    private StorageConfigService storageConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建文件配置")
    @PreAuthorize("@ss.hasPermission('infra:storage:config:create')")
    public CommonResult<String> createFileConfig(@Valid @RequestBody StorageConfigCreateReqVO createReqVO) {
        return CommonResult.success(storageConfigService.createFileConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文件配置")
    @PreAuthorize("@ss.hasPermission('infra:storage:config:update')")
    public CommonResult<Boolean> updateFileConfig(@Valid @RequestBody StorageConfigUpdateReqVO updateReqVO) {
        storageConfigService.updateFileConfig(updateReqVO);
        return CommonResult.success(true);
    }

    @PutMapping("/update-master")
    @Operation(summary = "更新文件配置为 Master")
    @PreAuthorize("@ss.hasPermission('infra:storage:config:update')")
    public CommonResult<Boolean> updateFileConfigMaster(@RequestParam("id") String id) {
        storageConfigService.updateFileConfigMaster(id);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('infra:storage:config:delete')")
    public CommonResult<Boolean> deleteFileConfig(@RequestParam("id") String id) {
        storageConfigService.deleteFileConfig(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文件配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:storage:config:query')")
    public CommonResult<StorageConfigRespVO> getStorageConfig(@RequestParam("id") String id) {
        StorageConfig storageConfig = storageConfigService.getStorageConfig(id);
        return CommonResult.success(StorageConfigConvert.INSTANCE.convert(storageConfig));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文件配置分页")
    @PreAuthorize("@ss.hasPermission('infra:storage:config:query')")
    public CommonResult<PageResult<StorageConfigRespVO>> getFileConfigPage(@Valid StorageConfigPageReqVO pageVO) {
        PageResult<StorageConfig> pageResult = storageConfigService.getStorageConfigPage(pageVO);
        return CommonResult.success(StorageConfigConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/test")
    @Operation(summary = "测试文件配置是否正确")
    @PreAuthorize("@ss.hasPermission('infra:storage:config:query')")
    public CommonResult<String> testFileConfig(@RequestParam("id") String id) throws Exception {
        String url = storageConfigService.testFileConfig(id);
        return CommonResult.success(url);
    }
}
