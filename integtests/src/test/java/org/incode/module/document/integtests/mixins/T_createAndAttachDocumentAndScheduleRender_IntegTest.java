/*
 *  Copyright 2014~2015 Dan Haywood
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
package org.incode.module.document.integtests.mixins;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.services.background.BackgroundCommandService;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.wrapper.DisabledException;

import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.module.document.fixture.dom.demo.DemoObject;
import org.incode.module.document.fixture.scripts.data.DemoObjectsFixture;
import org.incode.module.document.fixture.scripts.teardown.DocumentDemoAppTearDownFixture;
import org.incode.module.document.fixture.seed.DocumentTypeAndTemplatesApplicableForDemoObjectFixture;
import org.incode.module.document.integtests.DocumentModuleIntegTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assume.assumeThat;

public class T_createAndAttachDocumentAndScheduleRender_IntegTest extends DocumentModuleIntegTest {

    DemoObject demoObject;


    DocumentTypeAndTemplatesApplicableForDemoObjectFixture templateFs;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new DocumentDemoAppTearDownFixture(), null);

        // types + templates
        templateFs = new DocumentTypeAndTemplatesApplicableForDemoObjectFixture();
        fixtureScripts.runFixtureScript(templateFs, null);

        // demo objects
        final DemoObjectsFixture demoObjectsFixture = new DemoObjectsFixture();
        fixtureScripts.runFixtureScript(demoObjectsFixture, null);
        demoObject = demoObjectsFixture.getDemoObjects().get(0);

        transactionService.flushTransaction();
    }


    @Inject
    BackgroundCommandService backgroundCommandService;

    public static class Disabled_IntegTest extends T_createAndAttachDocumentAndScheduleRender_IntegTest {

        @Test
        public void if_no_background_service() throws Exception {

            // given
            assumeThat(backgroundCommandService, is(nullValue()));

            // when
            final TranslatableString reason = _createAndAttachDocumentAndScheduleRender(demoObject).disable$$();

            // then
            assertThat(reason).isNotNull();

            // expect
            expectedExceptions.expect(DisabledException.class);

            // when
            final DocumentTemplate anyTemplate = templateFs.getFmkTemplate();
            wrap(_createAndAttachDocumentAndScheduleRender(demoObject)).$$(anyTemplate);
        }
    }


}