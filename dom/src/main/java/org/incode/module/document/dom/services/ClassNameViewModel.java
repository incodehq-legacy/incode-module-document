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
package org.incode.module.document.dom.services;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Title;

import lombok.Getter;
import lombok.Setter;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "incodeDocuments.ClassNameViewModel"
)
public class ClassNameViewModel {

    public ClassNameViewModel() {
    }

    public ClassNameViewModel(final Class<?> cls) {
        this.simpleClassName = cls.getSimpleName();
        this.fullyQualifiedClassName = cls.getName();
    }

    @Title
    @Getter @Setter
    private String simpleClassName;
    @Getter @Setter
    private String fullyQualifiedClassName;

}
