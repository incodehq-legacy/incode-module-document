/*
 *  Copyright 2016 incode.org
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
package org.incode.module.document.dom.impl.applicability;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;

import org.incode.module.document.dom.DocumentModule;
import org.incode.module.document.dom.services.ClassNameViewModel;
import org.incode.module.document.dom.services.ClassService;
import org.incode.module.document.dom.spi.AttachmentAdvisorClassNameService;

@Mixin
public class Applicability_changeAttachmentAdvisor {


    //region > constructor
    private final Applicability applicability;

    public Applicability_changeAttachmentAdvisor(final Applicability applicability) {
        this.applicability = applicability;
    }
    //endregion


    public static class ActionDomainEvent extends DocumentModule.ActionDomainEvent<Applicability_changeAttachmentAdvisor>  { }

    @Action(
            semantics = SemanticsOf.IDEMPOTENT,
            domainEvent = ActionDomainEvent.class
    )
    @MemberOrder(name = "attachmentAdvisorClassName",sequence = "1")
    public Applicability_changeAttachmentAdvisor $$(final ClassNameViewModel classNameViewModel) {
        applicability.setAttachmentAdvisorClassName(classNameViewModel.getFullyQualifiedClassName());
        return this;
    }

    public TranslatableString disable$$() {
        return classNameService == null
                ? TranslatableString.tr(
                "No AttachmentAdvisorClassNameService registered to locate implementations of AttachmentAdvisor")
                : null;
    }

    public ClassNameViewModel default0$$() {
        return new ClassNameViewModel(classService.load(applicability.getAttachmentAdvisorClassName()));
    }

    public List<ClassNameViewModel> choices0$$() {
        return classNameService.attachmentAdvisorClassNames();
    }


    //region > injected services
    @Inject
    AttachmentAdvisorClassNameService classNameService;
    @Inject
    ClassService classService;
    //endregion
}
