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
package org.incode.module.document.fixture.seed;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.joda.time.LocalDate;

import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.value.Blob;
import org.apache.isis.applib.value.Clob;

import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.module.document.dom.impl.rendering.RenderingStrategy;
import org.incode.module.document.dom.impl.rendering.RenderingStrategyRepository;
import org.incode.module.document.dom.impl.types.DocumentType;
import org.incode.module.document.fixture.DocumentTemplateFSAbstract;
import org.incode.module.document.fixture.app.applicability.aa.ForDemoObjectAlsoAttachToFirstOtherObject;
import org.incode.module.document.fixture.app.applicability.aa.ForDemoObjectAttachToSame;
import org.incode.module.document.fixture.app.applicability.rmf.FreemarkerModelOfDemoObject;
import org.incode.module.document.fixture.app.applicability.rmf.StringInterpolatorRootOfDemoObject;
import org.incode.module.document.fixture.app.applicability.rmf.XDocReportModelOfDemoObject;
import org.incode.module.document.fixture.dom.demo.DemoObject;

import lombok.Getter;

public class DocumentTypeAndTemplatesApplicableForDemoObjectFixture extends DocumentTemplateFSAbstract {

    // applicable to DemoObject.class
    public static final String DOC_TYPE_REF_FREEMARKER_HTML = "FREEMARKER_HTML";
    public static final String DOC_TYPE_REF_STRINGINTERPOLATOR_URL = "STRINGINTERPOLATOR_URL";
    public static final String DOC_TYPE_REF_XDOCREPORT_DOC = "XDOCREPORT-DOC";
    public static final String DOC_TYPE_REF_XDOCREPORT_PDF = "XDOCREPORT-PDF";

    public static final String DOC_TYPE_REF_TAX_RECEIPT = "TAX_RECEIPT";
    public static final String DOC_TYPE_REF_SUPPLIER_RECEIPT = "SUPPLIER_RECEIPT";

    @Getter
    DocumentTemplate fmkTemplate;

    @Getter
    DocumentTemplate siTemplate;

    @Getter
    DocumentTemplate xdpTemplate;

    @Getter
    DocumentTemplate xddTemplate;

    @Override
    protected void execute(final ExecutionContext executionContext) {

        // prereqs
        executionContext.executeChild(this, new RenderingStrategiesFixture());

        // these document types have no associated templates (for attachPdf mixin)
        final DocumentType invoiceType =
                upsertType(DOC_TYPE_REF_TAX_RECEIPT, "Tax receipt", executionContext);
        final DocumentType docType =
                upsertType(DOC_TYPE_REF_SUPPLIER_RECEIPT, "Supplier receipt", executionContext);


        final DocumentType docTypeForFreemarkerHtml =
                upsertType(DOC_TYPE_REF_FREEMARKER_HTML, "Demo Freemarker HTML (eg email Cover Note)", executionContext);

        final RenderingStrategy fmkRenderingStrategy = renderingStrategyRepository.findByReference(RenderingStrategiesFixture.REF_FMK);
        final RenderingStrategy sipcRenderingStrategy = renderingStrategyRepository.findByReference(RenderingStrategiesFixture.REF_SIPC);
        final RenderingStrategy siRenderingStrategy = renderingStrategyRepository.findByReference(RenderingStrategiesFixture.REF_SI);
        final RenderingStrategy xdpRenderingStrategy = renderingStrategyRepository.findByReference(RenderingStrategiesFixture.REF_XDP);
        final RenderingStrategy xddRenderingStrategy = renderingStrategyRepository.findByReference(RenderingStrategiesFixture.REF_XDD);

        final String atPath = "/";


        //
        // freemarker template, with html
        //
        final LocalDate now = clockService.now();

        final Clob clob = new Clob(docTypeForFreemarkerHtml.getName(), "text/html",
                loadResource("FreemarkerHtmlCoverNote.html"));
        fmkTemplate = upsertDocumentClobTemplate(
                docTypeForFreemarkerHtml, now, atPath,
                ".html",
                false,
                clob, fmkRenderingStrategy,
                "Freemarker-html-cover-note-for-${demoObject.name}", fmkRenderingStrategy,
                executionContext);

        mixin(DocumentTemplate._applicable.class, fmkTemplate).applicable(
                DemoObject.class,
                FreemarkerModelOfDemoObject.class,
                ForDemoObjectAttachToSame.class);

        executionContext.addResult(this, fmkTemplate);



        //
        // template for string interpolator URL
        //
        final DocumentType docTypeForStringInterpolatorUrl =
                upsertType(DOC_TYPE_REF_STRINGINTERPOLATOR_URL, "Demo String Interpolator to retrieve URL", executionContext);

        siTemplate = upsertDocumentTextTemplate(
                docTypeForStringInterpolatorUrl, now, atPath,
                ".pdf",
                false,
                docTypeForStringInterpolatorUrl.getName(),
                "application/pdf",
                "${demoObject.url}", sipcRenderingStrategy,
                "pdf-of-url-held-in-${demoObject.name}", siRenderingStrategy,
                executionContext);

        mixin(DocumentTemplate._applicable.class, siTemplate).applicable(
                DemoObject.class,
                StringInterpolatorRootOfDemoObject.class,
                ForDemoObjectAttachToSame.class);



        //
        // template for xdocreport (PDF)
        //
        final DocumentType docTypeForXDocReportPdf =
                upsertType(DOC_TYPE_REF_XDOCREPORT_PDF, "Demo XDocReport for PDF", executionContext);

        xdpTemplate = upsertDocumentBlobTemplate(
                docTypeForXDocReportPdf, now, atPath,
                ".pdf",
                false,
                new Blob(
                        docTypeForXDocReportPdf.getName() + ".docx",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        loadResourceBytes("demoObject-template.docx")
                ), xdpRenderingStrategy,
                "${demoObject.name}", fmkRenderingStrategy,
                executionContext);

        mixin(DocumentTemplate._applicable.class, xdpTemplate).applicable(
                DemoObject.class,
                XDocReportModelOfDemoObject.class,
                ForDemoObjectAttachToSame.class);



        //
        // template for xdocreport (DOCX)
        //
        final DocumentType docTypeForXDocReportDocx =
                upsertType(DOC_TYPE_REF_XDOCREPORT_DOC, "Demo XDocReport for DOCX", executionContext);

        xddTemplate = upsertDocumentBlobTemplate(
                docTypeForXDocReportDocx, now, atPath,
                ".docx",
                false,
                new Blob(
                        docTypeForXDocReportDocx.getName() + ".docx",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        loadResourceBytes("demoObject-template.docx")
                ), xddRenderingStrategy,
                "${demoObject.name}", fmkRenderingStrategy,
                executionContext);

        mixin(DocumentTemplate._applicable.class, xddTemplate).applicable(
                DemoObject.class,
                XDocReportModelOfDemoObject.class,
                ForDemoObjectAlsoAttachToFirstOtherObject.class);

    }

    private static String loadResource(final String resourceName) {
        final URL templateUrl = Resources
                .getResource(DocumentTypeAndTemplatesApplicableForDemoObjectFixture.class, resourceName);
        try {
            return Resources.toString(templateUrl, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Unable to read resource URL '%s'", templateUrl));
        }
    }
    private static byte[] loadResourceBytes(final String resourceName) {
        final URL templateUrl = Resources
                .getResource(DocumentTypeAndTemplatesApplicableForDemoObjectFixture.class, resourceName);
        try {
            return Resources.toByteArray(templateUrl);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Unable to read resource URL '%s'", templateUrl));
        }
    }


    @Inject
    private RenderingStrategyRepository renderingStrategyRepository;
    @Inject
    private ClockService clockService;


}
