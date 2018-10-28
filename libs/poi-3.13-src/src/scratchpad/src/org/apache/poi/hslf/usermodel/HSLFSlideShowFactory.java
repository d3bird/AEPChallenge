/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hslf.usermodel;

import java.io.IOException;

import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.util.Internal;

@Internal
public class HSLFSlideShowFactory extends SlideShowFactory {
    /**
     * Creates a HSLFSlideShow from the given NPOIFSFileSystem
     * <p>Note that in order to properly release resources the
     *  SlideShow should be closed after use.
     */
    public static SlideShow<?,?> createSlideShow(NPOIFSFileSystem fs) throws IOException {
        return new HSLFSlideShow(fs);
    }

}
