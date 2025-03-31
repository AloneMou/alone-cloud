package com.alone.coder.module.system.init.vo;

import com.alone.coder.module.system.dal.dataobject.route.SystemRouteDO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RouteDefinitionVO {


    /**
     * 路由ID
     */
    private String id;
    /**
     * 路由名称
     */
    private String routeName;


    @NotEmpty
    @Valid
    private List<SystemRouteDO.PredicateDefinition> predicates = new ArrayList<>();

    @Valid
    private List<SystemRouteDO.PredicateDefinition> filters = new ArrayList<>();

    @NotNull
    private URI uri;

    private Map<String, Object> metadata = new HashMap<>();

    private int order = 0;

    private boolean enabled = true;

}
