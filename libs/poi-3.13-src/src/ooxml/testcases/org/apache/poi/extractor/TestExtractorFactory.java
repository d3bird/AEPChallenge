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
package org.apache.poi.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.poi.POIDataSamples;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hdgf.extractor.VisioTextExtractor;
import org.apache.poi.hpbf.extractor.PublisherTextExtractor;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hsmf.extractor.OutlookTextExtactor;
import org.apache.poi.hssf.extractor.EventBasedExcelExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

/**
 * Test that the extractor factory plays nicely
 */
public class TestExtractorFactory extends TestCase {
    private File txt;

    private File xls;
    private File xlsx;
    private File xlsxStrict;
    private File xltx;
    private File xlsEmb;

    private File doc;
    private File doc6;
    private File doc95;
    private File docx;
    private File dotx;
    private File docEmb;
    private File docEmbOOXML;

    private File ppt;
    private File pptx;

    private File msg;
    private File msgEmb;
    private File msgEmbMsg;

    private File vsd;
    private File vsdx;

    private File pub;

    private File getFileAndCheck(POIDataSamples samples, String name) {
        File file = samples.getFile(name);

        assertNotNull("Did not get a file for " + name, file);
        assertTrue("Did not get a type file for " + name, file.isFile());
        assertTrue("File did not exist: " + name, file.exists());

        return file;
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        POIDataSamples ssTests = POIDataSamples.getSpreadSheetInstance();
        xls = getFileAndCheck(ssTests, "SampleSS.xls");
        xlsx = getFileAndCheck(ssTests, "SampleSS.xlsx");
        xlsxStrict = getFileAndCheck(ssTests, "SampleSS.strict.xlsx");
        xltx = getFileAndCheck(ssTests, "test.xltx");
        xlsEmb = getFileAndCheck(ssTests, "excel_with_embeded.xls");

        POIDataSamples wpTests = POIDataSamples.getDocumentInstance();
        doc = getFileAndCheck(wpTests, "SampleDoc.doc");
        doc6 = getFileAndCheck(wpTests, "Word6.doc");
        doc95 = getFileAndCheck(wpTests, "Word95.doc");
        docx = getFileAndCheck(wpTests, "SampleDoc.docx");
        dotx = getFileAndCheck(wpTests, "test.dotx");
        docEmb = getFileAndCheck(wpTests, "word_with_embeded.doc");
        docEmbOOXML = getFileAndCheck(wpTests, "word_with_embeded_ooxml.doc");

        POIDataSamples slTests = POIDataSamples.getSlideShowInstance();
        ppt = getFileAndCheck(slTests, "SampleShow.ppt");
        pptx = getFileAndCheck(slTests, "SampleShow.pptx");
        txt = getFileAndCheck(slTests, "SampleShow.txt");

        POIDataSamples dgTests = POIDataSamples.getDiagramInstance();
        vsd = getFileAndCheck(dgTests, "Test_Visio-Some_Random_Text.vsd");
        vsdx = getFileAndCheck(dgTests, "test.vsdx");

        POIDataSamples pubTests = POIDataSamples.getPublisherInstance();
        pub = getFileAndCheck(pubTests, "Simple.pub");

        POIDataSamples olTests = POIDataSamples.getHSMFInstance();
        msg = getFileAndCheck(olTests, "quick.msg");
        msgEmb = getFileAndCheck(olTests, "attachment_test_msg.msg");
        msgEmbMsg = getFileAndCheck(olTests, "attachment_msg_pdf.msg");
    }

    public void testFile() throws Exception {
        // Excel
        POITextExtractor xlsExtractor = ExtractorFactory.createExtractor(xls);
        assertNotNull("Had empty extractor for " + xls, xlsExtractor);
        assertTrue("Expected instanceof ExcelExtractor, but had: " + xlsExtractor.getClass(),
                xlsExtractor
                instanceof ExcelExtractor
        );
        assertTrue(
                xlsExtractor.getText().length() > 200
        );
        xlsExtractor.close();

        POITextExtractor extractor = ExtractorFactory.createExtractor(xlsx);
        assertTrue(
                extractor
                instanceof XSSFExcelExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(xlsx);
        assertTrue(
                extractor.getText().length() > 200
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(xltx);
        assertTrue(
                extractor
                instanceof XSSFExcelExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(xltx);
        assertTrue(
                extractor.getText().contains("test")
        );
        extractor.close();

        // TODO Support OOXML-Strict, see bug #57699
        try {
            extractor = ExtractorFactory.createExtractor(xlsxStrict);
            fail("OOXML-Strict isn't yet supported");
        } catch (POIXMLException e) {
            // Expected, for now
        }
//        extractor = ExtractorFactory.createExtractor(xlsxStrict);
//        assertTrue(
//                extractor
//                instanceof XSSFExcelExtractor
//        );
//        extractor.close();
//
//        extractor = ExtractorFactory.createExtractor(xlsxStrict);
//        assertTrue(
//                extractor.getText().contains("test")
//        );
//        extractor.close();


        // Word
        assertTrue(
                ExtractorFactory.createExtractor(doc)
                instanceof WordExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(doc).getText().length() > 120
        );

        assertTrue(
                ExtractorFactory.createExtractor(doc6)
                instanceof Word6Extractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(doc6).getText().length() > 20
        );

        assertTrue(
                ExtractorFactory.createExtractor(doc95)
                instanceof Word6Extractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(doc95).getText().length() > 120
        );

        extractor = ExtractorFactory.createExtractor(docx);
        assertTrue(
                extractor instanceof XWPFWordExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(docx);
        assertTrue(
                extractor.getText().length() > 120
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(dotx);
        assertTrue(
                extractor instanceof XWPFWordExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(dotx);
        assertTrue(
                extractor.getText().contains("Test")
        );
        extractor.close();

        // PowerPoint
        assertTrue(
                ExtractorFactory.createExtractor(ppt)
                instanceof PowerPointExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(ppt).getText().length() > 120
        );

        extractor = ExtractorFactory.createExtractor(pptx);
        assertTrue(
                extractor
                instanceof XSLFPowerPointExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(pptx);
        assertTrue(
                extractor.getText().length() > 120
        );
        extractor.close();

        // Visio - binary
        assertTrue(
                ExtractorFactory.createExtractor(vsd)
                instanceof VisioTextExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(vsd).getText().length() > 50
        );
        // Visio - vsdx
        try {
            ExtractorFactory.createExtractor(vsdx);
            fail();
        } catch(IllegalArgumentException e) {
            // Good
        }

        // Publisher
        assertTrue(
                ExtractorFactory.createExtractor(pub)
                instanceof PublisherTextExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(pub).getText().length() > 50
        );

        // Outlook msg
        assertTrue(
                ExtractorFactory.createExtractor(msg)
                instanceof OutlookTextExtactor
        );
        assertTrue(
                ExtractorFactory.createExtractor(msg).getText().length() > 50
        );

        // Text
        try {
            ExtractorFactory.createExtractor(txt);
            fail();
        } catch(IllegalArgumentException e) {
            // Good
        }
    }

    public void testInputStream() throws Exception {
        // Excel
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(xls))
                instanceof ExcelExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(xls)).getText().length() > 200
        );

        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(xlsx))
                instanceof XSSFExcelExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(xlsx)).getText().length() > 200
        );
        // TODO Support OOXML-Strict, see bug #57699
//        assertTrue(
//                ExtractorFactory.createExtractor(new FileInputStream(xlsxStrict))
//                instanceof XSSFExcelExtractor
//        );
//        assertTrue(
//                ExtractorFactory.createExtractor(new FileInputStream(xlsxStrict)).getText().length() > 200
//        );

        // Word
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(doc))
                instanceof WordExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(doc)).getText().length() > 120
        );

        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(doc6))
                instanceof Word6Extractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(doc6)).getText().length() > 20
        );

        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(doc95))
                instanceof Word6Extractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(doc95)).getText().length() > 120
        );

        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(docx))
                instanceof XWPFWordExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(docx)).getText().length() > 120
        );

        // PowerPoint
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(ppt))
                instanceof PowerPointExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(ppt)).getText().length() > 120
        );

        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(pptx))
                instanceof XSLFPowerPointExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(pptx)).getText().length() > 120
        );

        // Visio
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(vsd))
                instanceof VisioTextExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(vsd)).getText().length() > 50
        );
        // Visio - vsdx
        try {
            ExtractorFactory.createExtractor(new FileInputStream(vsdx));
            fail();
        } catch(IllegalArgumentException e) {
            // Good
        }

        // Publisher
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(pub))
                instanceof PublisherTextExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(pub)).getText().length() > 50
        );

        // Outlook msg
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(msg))
                instanceof OutlookTextExtactor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new FileInputStream(msg)).getText().length() > 50
        );

        // Text
        try {
            FileInputStream stream = new FileInputStream(txt);
            try {
                ExtractorFactory.createExtractor(stream);
                fail();
            } finally {
                stream.close();
            }
        } catch(IllegalArgumentException e) {
            // Good
        }
    }

    public void testPOIFS() throws Exception {
        // Excel
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(xls)))
                instanceof ExcelExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(xls))).getText().length() > 200
        );

        // Word
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(doc)))
                instanceof WordExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(doc))).getText().length() > 120
        );

        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(doc6)))
                instanceof Word6Extractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(doc6))).getText().length() > 20
        );

        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(doc95)))
                instanceof Word6Extractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(doc95))).getText().length() > 120
        );

        // PowerPoint
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(ppt)))
                instanceof PowerPointExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(ppt))).getText().length() > 120
        );

        // Visio
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(vsd)))
                instanceof VisioTextExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(vsd))).getText().length() > 50
        );

        // Publisher
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(pub)))
                instanceof PublisherTextExtractor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(pub))).getText().length() > 50
        );

        // Outlook msg
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(msg)))
                instanceof OutlookTextExtactor
        );
        assertTrue(
                ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(msg))).getText().length() > 50
        );

        // Text
        try {
            ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(txt)));
            fail();
        } catch(IOException e) {
            // Good
        }
    }

    public void testPackage() throws Exception {
        // Excel
        POIXMLTextExtractor extractor = ExtractorFactory.createExtractor(OPCPackage.open(xlsx.toString(), PackageAccess.READ));
        assertTrue(
                extractor
                instanceof XSSFExcelExtractor
        );
        extractor.close();
        extractor = ExtractorFactory.createExtractor(OPCPackage.open(xlsx.toString()));
        assertTrue(extractor.getText().length() > 200);
        extractor.close();

        // Word
        extractor = ExtractorFactory.createExtractor(OPCPackage.open(docx.toString()));
        assertTrue(
                extractor
                instanceof XWPFWordExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(OPCPackage.open(docx.toString()));
        assertTrue(
                extractor.getText().length() > 120
        );
        extractor.close();

        // PowerPoint
        extractor = ExtractorFactory.createExtractor(OPCPackage.open(pptx.toString()));
        assertTrue(
                extractor
                instanceof XSLFPowerPointExtractor
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(OPCPackage.open(pptx.toString()));
        assertTrue(
                extractor.getText().length() > 120
        );
        extractor.close();

        // Text
        try {
            ExtractorFactory.createExtractor(OPCPackage.open(txt.toString()));
            fail();
        } catch(InvalidOperationException e) {
            // Good
        }
    }

    public void testPreferEventBased() throws Exception {
        assertFalse(ExtractorFactory.getPreferEventExtractor());
        assertFalse(ExtractorFactory.getThreadPrefersEventExtractors());
        assertNull(ExtractorFactory.getAllThreadsPreferEventExtractors());

        ExtractorFactory.setThreadPrefersEventExtractors(true);

        assertTrue(ExtractorFactory.getPreferEventExtractor());
        assertTrue(ExtractorFactory.getThreadPrefersEventExtractors());
        assertNull(ExtractorFactory.getAllThreadsPreferEventExtractors());

        ExtractorFactory.setAllThreadsPreferEventExtractors(false);

        assertFalse(ExtractorFactory.getPreferEventExtractor());
        assertTrue(ExtractorFactory.getThreadPrefersEventExtractors());
        assertEquals(Boolean.FALSE, ExtractorFactory.getAllThreadsPreferEventExtractors());

        ExtractorFactory.setAllThreadsPreferEventExtractors(null);

        assertTrue(ExtractorFactory.getPreferEventExtractor());
        assertTrue(ExtractorFactory.getThreadPrefersEventExtractors());
        assertNull(ExtractorFactory.getAllThreadsPreferEventExtractors());


        // Check we get the right extractors now
        POITextExtractor extractor = ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(xls)));
        assertTrue(
                extractor
                instanceof EventBasedExcelExtractor
        );
        extractor.close();
        extractor = ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(xls)));
        assertTrue(
                extractor.getText().length() > 200
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(OPCPackage.open(xlsx.toString(), PackageAccess.READ));
        assertTrue(extractor instanceof XSSFEventBasedExcelExtractor);
        extractor.close();

        extractor = ExtractorFactory.createExtractor(OPCPackage.open(xlsx.toString(), PackageAccess.READ));
        assertTrue(
                extractor.getText().length() > 200
        );
        extractor.close();


        // Put back to normal
        ExtractorFactory.setThreadPrefersEventExtractors(false);
        assertFalse(ExtractorFactory.getPreferEventExtractor());
        assertFalse(ExtractorFactory.getThreadPrefersEventExtractors());
        assertNull(ExtractorFactory.getAllThreadsPreferEventExtractors());

        // And back
        extractor = ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(xls)));
        assertTrue(
                extractor
                instanceof ExcelExtractor
        );
        extractor.close();
        extractor = ExtractorFactory.createExtractor(new POIFSFileSystem(new FileInputStream(xls)));
        assertTrue(
                extractor.getText().length() > 200
        );
        extractor.close();

        extractor = ExtractorFactory.createExtractor(OPCPackage.open(xlsx.toString(), PackageAccess.READ));
        assertTrue(
                extractor
                instanceof XSSFExcelExtractor
        );
        extractor.close();
        extractor = ExtractorFactory.createExtractor(OPCPackage.open(xlsx.toString()));
        assertTrue(
                extractor.getText().length() > 200
        );
        extractor.close();
    }

    /**
     * Test embeded docs text extraction. For now, only
     *  does poifs embeded, but will do ooxml ones
     *  at some point.
     */
    public void testEmbeded() throws Exception {
        POIOLE2TextExtractor ext;
        POITextExtractor[] embeds;

        // No embedings
        ext = (POIOLE2TextExtractor)
                ExtractorFactory.createExtractor(xls);
        embeds = ExtractorFactory.getEmbededDocsTextExtractors(ext);
        assertEquals(0, embeds.length);

        // Excel
        ext = (POIOLE2TextExtractor)
                ExtractorFactory.createExtractor(xlsEmb);
        embeds = ExtractorFactory.getEmbededDocsTextExtractors(ext);

        assertEquals(6, embeds.length);
        int numWord = 0, numXls = 0, numPpt = 0, numMsg = 0, numWordX;
        for(int i=0; i<embeds.length; i++) {
            assertTrue(embeds[i].getText().length() > 20);

            if(embeds[i] instanceof PowerPointExtractor) numPpt++;
            else if(embeds[i] instanceof ExcelExtractor) numXls++;
            else if(embeds[i] instanceof WordExtractor) numWord++;
            else if(embeds[i] instanceof OutlookTextExtactor) numMsg++;
        }
        assertEquals(2, numPpt);
        assertEquals(2, numXls);
        assertEquals(2, numWord);
        assertEquals(0, numMsg);

        // Word
        ext = (POIOLE2TextExtractor)
                ExtractorFactory.createExtractor(docEmb);
        embeds = ExtractorFactory.getEmbededDocsTextExtractors(ext);

        numWord = 0; numXls = 0; numPpt = 0; numMsg = 0;
        assertEquals(4, embeds.length);
        for(int i=0; i<embeds.length; i++) {
            assertTrue(embeds[i].getText().length() > 20);
            if(embeds[i] instanceof PowerPointExtractor) numPpt++;
            else if(embeds[i] instanceof ExcelExtractor) numXls++;
            else if(embeds[i] instanceof WordExtractor) numWord++;
            else if(embeds[i] instanceof OutlookTextExtactor) numMsg++;
        }
        assertEquals(1, numPpt);
        assertEquals(2, numXls);
        assertEquals(1, numWord);
        assertEquals(0, numMsg);

        // Word which contains an OOXML file
        ext = (POIOLE2TextExtractor)
                ExtractorFactory.createExtractor(docEmbOOXML);
        embeds = ExtractorFactory.getEmbededDocsTextExtractors(ext);

        numWord = 0; numXls = 0; numPpt = 0; numMsg = 0; numWordX = 0;
        assertEquals(3, embeds.length);
        for(int i=0; i<embeds.length; i++) {
            assertTrue(embeds[i].getText().length() > 20);
            if(embeds[i] instanceof PowerPointExtractor) numPpt++;
            else if(embeds[i] instanceof ExcelExtractor) numXls++;
            else if(embeds[i] instanceof WordExtractor) numWord++;
            else if(embeds[i] instanceof OutlookTextExtactor) numMsg++;
            else if(embeds[i] instanceof XWPFWordExtractor) numWordX++;
        }
        assertEquals(1, numPpt);
        assertEquals(1, numXls);
        assertEquals(0, numWord);
        assertEquals(1, numWordX);
        assertEquals(0, numMsg);

        // Outlook
        ext = (OutlookTextExtactor)
                ExtractorFactory.createExtractor(msgEmb);
        embeds = ExtractorFactory.getEmbededDocsTextExtractors(ext);

        numWord = 0; numXls = 0; numPpt = 0; numMsg = 0;
        assertEquals(1, embeds.length);
        for(int i=0; i<embeds.length; i++) {
            assertTrue(embeds[i].getText().length() > 20);
            if(embeds[i] instanceof PowerPointExtractor) numPpt++;
            else if(embeds[i] instanceof ExcelExtractor) numXls++;
            else if(embeds[i] instanceof WordExtractor) numWord++;
            else if(embeds[i] instanceof OutlookTextExtactor) numMsg++;
        }
        assertEquals(0, numPpt);
        assertEquals(0, numXls);
        assertEquals(1, numWord);
        assertEquals(0, numMsg);

        // Outlook with another outlook file in it
        ext = (OutlookTextExtactor)
                ExtractorFactory.createExtractor(msgEmbMsg);
        embeds = ExtractorFactory.getEmbededDocsTextExtractors(ext);

        numWord = 0; numXls = 0; numPpt = 0; numMsg = 0;
        assertEquals(1, embeds.length);
        for(int i=0; i<embeds.length; i++) {
            assertTrue(embeds[i].getText().length() > 20);
            if(embeds[i] instanceof PowerPointExtractor) numPpt++;
            else if(embeds[i] instanceof ExcelExtractor) numXls++;
            else if(embeds[i] instanceof WordExtractor) numWord++;
            else if(embeds[i] instanceof OutlookTextExtactor) numMsg++;
        }
        assertEquals(0, numPpt);
        assertEquals(0, numXls);
        assertEquals(0, numWord);
        assertEquals(1, numMsg);


        // TODO - PowerPoint
        // TODO - Publisher
        // TODO - Visio
    }
}
