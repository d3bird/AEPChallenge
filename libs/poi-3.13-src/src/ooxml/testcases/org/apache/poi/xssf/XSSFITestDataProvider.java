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

package org.apache.poi.xssf;

import org.apache.poi.POIDataSamples;
import org.apache.poi.ss.ITestDataProvider;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Yegor Kozlov
 */
public final class XSSFITestDataProvider implements ITestDataProvider {
    public static final XSSFITestDataProvider instance = new XSSFITestDataProvider();

    private XSSFITestDataProvider() {
        // enforce singleton
    }

    public XSSFWorkbook openSampleWorkbook(String sampleFileName) {
        return XSSFTestDataSamples.openSampleWorkbook(sampleFileName);
    }

    public XSSFWorkbook writeOutAndReadBack(Workbook original) {
        if(!(original instanceof XSSFWorkbook)) {
            throw new IllegalArgumentException("Expected an instance of XSSFWorkbook, but had " + original.getClass());
        }
        return XSSFTestDataSamples.writeOutAndReadBack((XSSFWorkbook)original);
    }

    public XSSFWorkbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public FormulaEvaluator createFormulaEvaluator(Workbook wb) {
        return new XSSFFormulaEvaluator((XSSFWorkbook) wb);
    }

    public byte[] getTestDataFileContent(String fileName) {
        return POIDataSamples.getSpreadSheetInstance().readFile(fileName);
    }

    public SpreadsheetVersion getSpreadsheetVersion(){
        return SpreadsheetVersion.EXCEL2007;
    }

    public String getStandardFileNameExtension() {
        return "xlsx";
    }
}
