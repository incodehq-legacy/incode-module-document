/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.module.document.fixture.app.binders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.isisaddons.module.xdocreport.dom.service.XDocReportModel;

import org.incode.module.document.dom.impl.applicability.Binder;
import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.module.document.fixture.dom.demo.DemoObject;

import lombok.Getter;

public class BinderOfXDocReportModelForDemoObject implements Binder {

    public Binding newBinding(
            final DocumentTemplate documentTemplate,
            final Object domainObject, final String additionalTextIfAny) {

        if(!(domainObject instanceof DemoObject)) {
            throw new IllegalArgumentException("Domain object must be of type DemoObject");
        }
        DemoObject demoObject = (DemoObject) domainObject;

        // dataModel
        final DataModel dataModel = new DataModel(demoObject);

        // binding
        return new Binding(dataModel, determineAttachTo(domainObject));
    }

    protected List<Object> determineAttachTo(final Object domainObject) {
        return Collections.singletonList(domainObject);
    }

    public static class DataModel implements XDocReportModel {

        // for freemarker
        @Getter
        private final DemoObject demoObject;

        public DataModel(final DemoObject demoObject) {
            this.demoObject = demoObject;
        }

        // for XDocReport
        @Override
        public Map<String, Data> getContextData() {
            return ImmutableMap.of("demoObject", Data.object(demoObject));
        }

    }
}