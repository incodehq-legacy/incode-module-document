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
package org.incode.module.document.dom.impl.renderers;

import java.io.IOException;

import org.incode.module.document.dom.impl.types.DocumentType;

public interface RendererFromBytesToChars extends Renderer {

    String renderBytesToChars(
            final DocumentType documentType,
            final String variant,
            final String atPath,
            final long templateVersion,
            final byte[] templateBytes,
            final Object dataModel) throws IOException;

}
