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

package org.apache.dolphinscheduler.api.test.pages;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.dolphinscheduler.api.test.base.IPageAPI;
import org.apache.dolphinscheduler.api.test.core.common.Constants;
import org.apache.dolphinscheduler.api.test.core.common.FormParam;
import org.apache.dolphinscheduler.api.test.pages.login.form.LoginFormData;
import org.apache.dolphinscheduler.api.test.utils.RestResponse;
import org.apache.dolphinscheduler.api.test.utils.Result;


public class PageAPI implements IPageAPI {
    public static RestResponse<Result> getSession(RequestSpecification request, RequestSpecification reqSpec) {
        Response resp = request.
                spec(reqSpec).
                formParam(LoginFormData.USR_NAME.getParam(), LoginFormData.USR_NAME.getData()).
                formParam(LoginFormData.USER_PASSWD.getParam(), LoginFormData.USER_PASSWD.getData()).
                when().
                post(Route.login());
        return new RestResponse<>(Result.class, resp);
    }

    public static RestResponse<Result> releaseSession(RequestSpecification request, RequestSpecification reqSpec, String sessionId) {
        Response resp = request.
                spec(reqSpec).
                cookies(FormParam.SESSION_ID.getParam(), sessionId).
                when().
                post(Route.loginOut());
        return new RestResponse<>(Result.class, resp);
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder().
                setBaseUri(Constants.DOLPHINSCHEDULER_BASE_URL).
                setPort(Constants.DOLPHINSCHEDULER_API_PORT).
                setBasePath(Constants.DOLPHINSCHEDULER_BASE_PATH).build();
    }

}
