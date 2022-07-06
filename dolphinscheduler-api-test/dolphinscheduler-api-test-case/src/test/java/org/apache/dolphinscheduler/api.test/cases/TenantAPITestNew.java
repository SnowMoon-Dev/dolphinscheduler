/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.dolphinscheduler.api.test.cases;


import com.devskiller.jfairy.Fairy;
import org.apache.dolphinscheduler.api.test.base.AbstractAPITest;
import org.apache.dolphinscheduler.api.test.core.common.FormParam;
import org.apache.dolphinscheduler.api.test.core.extensions.DolphinScheduler;
import org.apache.dolphinscheduler.api.test.entity.PageParamEntity;
import org.apache.dolphinscheduler.api.test.pages.security.tenant.TenantPageAPI;
import org.apache.dolphinscheduler.api.test.pages.security.tenant.entity.TenantRequestEntity;
import org.apache.dolphinscheduler.api.test.pages.security.tenant.entity.TenantResponseEntity;
import org.apache.dolphinscheduler.api.test.utils.RestResponse;
import org.apache.dolphinscheduler.api.test.utils.Result;
import org.apache.dolphinscheduler.api.test.utils.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;


@DolphinScheduler(composeFiles = "docker/basic/docker-compose.yaml")
@DisplayName("Tenant API interface test")
public class TenantAPITestNew extends AbstractAPITest {
    protected final Fairy fairy = Fairy.create();
    private TenantResponseEntity tenantResponseEntity = null;
    private TenantPageAPI tenantPageAPI = null;

    @BeforeAll
    public void initTenantEndPointFactory() {
        tenantPageAPI = pageAPIFactory.createTenantPageAPI();
    }


    @Test
    @Order(1)
    @DisplayName("Test the correct Tenant information to log in to the system")
    public void testCreateTenant() {
        TenantRequestEntity tenantRequestEntity = new TenantRequestEntity();
        tenantRequestEntity.setTenantCode(fairy.person().getFullName());
        tenantRequestEntity.setQueueId(1);
        tenantRequestEntity.setDescription(fairy.person().getFullName());
        RestResponse<Result> result = tenantPageAPI.createTenant(tenantRequestEntity);
        result.isResponseSuccessful();
        tenantResponseEntity = result.getResponse().jsonPath().getObject(FormParam.DATA.getParam(), TenantResponseEntity.class);
    }


    @Test
    @Order(2)
    public void testUpdateTenant() {
        testCreateTenant();
        TenantRequestEntity tenantUpdateEntity = new TenantRequestEntity();
        tenantUpdateEntity.setId(tenantResponseEntity.getId());
        tenantUpdateEntity.setTenantCode(tenantResponseEntity.getTenantCode());
        tenantUpdateEntity.setQueueId(1);
        tenantUpdateEntity.setDescription(fairy.person().getMobileTelephoneNumber());
        tenantPageAPI.updateTenant(tenantUpdateEntity, tenantResponseEntity.getId()).isResponseSuccessful();
    }

    @Test
    @Order(3)
    @DisplayName("Verify that the existing tenant returns code 10009")
    public void testVerifyExistTenantCode() {
        tenantPageAPI.verifyTenantCode(tenantResponseEntity.getTenantCode()).getResponse().then().
                body(FormParam.CODE.getParam(), equalTo(Status.OS_TENANT_CODE_EXIST.getCode()));
    }


    @Test
    @Order(4)
    public void testQueryTenantlistPaging() {
        PageParamEntity pageParamEntity = new PageParamEntity();
        pageParamEntity.setPageNo(1);
        pageParamEntity.setPageSize(10);
        pageParamEntity.setSearchVal("");
        tenantPageAPI.getTenants(pageParamEntity).isResponseSuccessful();
    }


    @Test
    @Order(5)
    public void testQueryTenantListAll() {
        tenantPageAPI.getTenantsListAll().getResponse().prettyPrint();
    }



    @Test
    @Order(6)
    @DisplayName("Verify that the non-existent tenant returns code 0")
    public void testVerifyNotExistTenantCode() {
        TenantRequestEntity tenantRequestEntity = new TenantRequestEntity();
        tenantRequestEntity.setTenantCode(fairy.person().getCompany().getName());
        tenantPageAPI.verifyTenantCode(tenantRequestEntity.getTenantCode()).getResponse().prettyPrint();
    }


    @Test
    @Order(7)
    @DisplayName("delete exist tenant by tenant id")
    public void testDeleteExistTenantByCode() {
        tenantPageAPI.deleteTenantById(tenantResponseEntity.getId()).getResponse().prettyPrint();
    }

}
