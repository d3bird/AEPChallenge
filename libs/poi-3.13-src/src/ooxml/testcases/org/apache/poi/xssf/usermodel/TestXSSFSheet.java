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

package org.apache.poi.xssf.usermodel;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.apache.poi.xssf.XSSFTestDataSamples.openSampleWorkbook;
import static org.apache.poi.xssf.XSSFTestDataSamples.writeOutAndReadBack;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.HSSFTestDataSamples;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.AutoFilter;
import org.apache.poi.ss.usermodel.BaseTestSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.XSSFITestDataProvider;
import org.apache.poi.xssf.XSSFTestDataSamples;
import org.apache.poi.xssf.model.CalculationChain;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.*;


@SuppressWarnings("resource")
public final class TestXSSFSheet extends BaseTestSheet {

    public TestXSSFSheet() {
        super(XSSFITestDataProvider.instance);
    }

    //TODO column styles are not yet supported by XSSF
    @Override
    @Test
    public void defaultColumnStyle() {
        //super.defaultColumnStyle();
    }

    @Test
    public void getSetMargin() {
        baseTestGetSetMargin(new double[]{0.7, 0.7, 0.75, 0.75, 0.3, 0.3});
    }

    @Test
    public void existingHeaderFooter() {
        XSSFWorkbook workbook = XSSFTestDataSamples.openSampleWorkbook("45540_classic_Header.xlsx");
        XSSFOddHeader hdr;
        XSSFOddFooter ftr;

        // Sheet 1 has a header with center and right text
        XSSFSheet s1 = workbook.getSheetAt(0);
        assertNotNull(s1.getHeader());
        assertNotNull(s1.getFooter());
        hdr = (XSSFOddHeader) s1.getHeader();
        ftr = (XSSFOddFooter) s1.getFooter();

        assertEquals("&Ctestdoc&Rtest phrase", hdr.getText());
        assertEquals(null, ftr.getText());

        assertEquals("", hdr.getLeft());
        assertEquals("testdoc", hdr.getCenter());
        assertEquals("test phrase", hdr.getRight());

        assertEquals("", ftr.getLeft());
        assertEquals("", ftr.getCenter());
        assertEquals("", ftr.getRight());

        // Sheet 2 has a footer, but it's empty
        XSSFSheet s2 = workbook.getSheetAt(1);
        assertNotNull(s2.getHeader());
        assertNotNull(s2.getFooter());
        hdr = (XSSFOddHeader) s2.getHeader();
        ftr = (XSSFOddFooter) s2.getFooter();

        assertEquals(null, hdr.getText());
        assertEquals("&L&F", ftr.getText());

        assertEquals("", hdr.getLeft());
        assertEquals("", hdr.getCenter());
        assertEquals("", hdr.getRight());

        assertEquals("&F", ftr.getLeft());
        assertEquals("", ftr.getCenter());
        assertEquals("", ftr.getRight());

        // Save and reload
        XSSFWorkbook wb = XSSFTestDataSamples.writeOutAndReadBack(workbook);

        hdr = (XSSFOddHeader) wb.getSheetAt(0).getHeader();
        ftr = (XSSFOddFooter) wb.getSheetAt(0).getFooter();

        assertEquals("", hdr.getLeft());
        assertEquals("testdoc", hdr.getCenter());
        assertEquals("test phrase", hdr.getRight());

        assertEquals("", ftr.getLeft());
        assertEquals("", ftr.getCenter());
        assertEquals("", ftr.getRight());
    }

    @Test
    public void getAllHeadersFooters() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        assertNotNull(sheet.getOddFooter());
        assertNotNull(sheet.getEvenFooter());
        assertNotNull(sheet.getFirstFooter());
        assertNotNull(sheet.getOddHeader());
        assertNotNull(sheet.getEvenHeader());
        assertNotNull(sheet.getFirstHeader());

        assertEquals("", sheet.getOddFooter().getLeft());
        sheet.getOddFooter().setLeft("odd footer left");
        assertEquals("odd footer left", sheet.getOddFooter().getLeft());

        assertEquals("", sheet.getEvenFooter().getLeft());
        sheet.getEvenFooter().setLeft("even footer left");
        assertEquals("even footer left", sheet.getEvenFooter().getLeft());

        assertEquals("", sheet.getFirstFooter().getLeft());
        sheet.getFirstFooter().setLeft("first footer left");
        assertEquals("first footer left", sheet.getFirstFooter().getLeft());

        assertEquals("", sheet.getOddHeader().getLeft());
        sheet.getOddHeader().setLeft("odd header left");
        assertEquals("odd header left", sheet.getOddHeader().getLeft());

        assertEquals("", sheet.getOddHeader().getRight());
        sheet.getOddHeader().setRight("odd header right");
        assertEquals("odd header right", sheet.getOddHeader().getRight());

        assertEquals("", sheet.getOddHeader().getCenter());
        sheet.getOddHeader().setCenter("odd header center");
        assertEquals("odd header center", sheet.getOddHeader().getCenter());

        // Defaults are odd
        assertEquals("odd footer left", sheet.getFooter().getLeft());
        assertEquals("odd header center", sheet.getHeader().getCenter());
    }

    @Test
    public void autoSizeColumn() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        sheet.createRow(0).createCell(13).setCellValue("test");

        sheet.autoSizeColumn(13);

        ColumnHelper columnHelper = sheet.getColumnHelper();
        CTCol col = columnHelper.getColumn(13, false);
        assertTrue(col.getBestFit());
    }


    @Test
    public void setCellComment() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        XSSFDrawing dg = sheet.createDrawingPatriarch();
        XSSFComment comment = dg.createCellComment(new XSSFClientAnchor());

        Cell cell = sheet.createRow(0).createCell(0);
        CommentsTable comments = sheet.getCommentsTable(false);
        CTComments ctComments = comments.getCTComments();

        cell.setCellComment(comment);
        assertEquals("A1", ctComments.getCommentList().getCommentArray(0).getRef());
        comment.setAuthor("test A1 author");
        assertEquals("test A1 author", comments.getAuthor((int) ctComments.getCommentList().getCommentArray(0).getAuthorId()));
    }

    @Test
    public void getActiveCell() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.setActiveCell("R5");

        assertEquals("R5", sheet.getActiveCell());

    }

    @Test
    public void createFreezePane_XSSF() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CTWorksheet ctWorksheet = sheet.getCTWorksheet();

        sheet.createFreezePane(2, 4);
        assertEquals(2.0, ctWorksheet.getSheetViews().getSheetViewArray(0).getPane().getXSplit(), 0.0);
        assertEquals(STPane.BOTTOM_RIGHT, ctWorksheet.getSheetViews().getSheetViewArray(0).getPane().getActivePane());
        sheet.createFreezePane(3, 6, 10, 10);
        assertEquals(3.0, ctWorksheet.getSheetViews().getSheetViewArray(0).getPane().getXSplit(), 0.0);
        //    assertEquals(10, sheet.getTopRow());
        //    assertEquals(10, sheet.getLeftCol());
        sheet.createSplitPane(4, 8, 12, 12, 1);
        assertEquals(8.0, ctWorksheet.getSheetViews().getSheetViewArray(0).getPane().getYSplit(), 0.0);
        assertEquals(STPane.BOTTOM_RIGHT, ctWorksheet.getSheetViews().getSheetViewArray(0).getPane().getActivePane());
    }

    @Test
    public void removeMergedRegion_lowlevel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CTWorksheet ctWorksheet = sheet.getCTWorksheet();
        CellRangeAddress region_1 = CellRangeAddress.valueOf("A1:B2");
        CellRangeAddress region_2 = CellRangeAddress.valueOf("C3:D4");
        CellRangeAddress region_3 = CellRangeAddress.valueOf("E5:F6");
        CellRangeAddress region_4 = CellRangeAddress.valueOf("G7:H8");
        sheet.addMergedRegion(region_1);
        sheet.addMergedRegion(region_2);
        sheet.addMergedRegion(region_3);
        assertEquals("C3:D4", ctWorksheet.getMergeCells().getMergeCellArray(1).getRef());
        assertEquals(3, sheet.getNumMergedRegions());
        sheet.removeMergedRegion(1);
        assertEquals("E5:F6", ctWorksheet.getMergeCells().getMergeCellArray(1).getRef());
        assertEquals(2, sheet.getNumMergedRegions());
        sheet.removeMergedRegion(1);
        sheet.removeMergedRegion(0);
        assertEquals(0, sheet.getNumMergedRegions());
        assertNull(" CTMergeCells should be deleted after removing the last merged " +
                "region on the sheet.", sheet.getCTWorksheet().getMergeCells());
        sheet.addMergedRegion(region_1);
        sheet.addMergedRegion(region_2);
        sheet.addMergedRegion(region_3);
        sheet.addMergedRegion(region_4);
        // test invalid indexes OOBE
        Set<Integer> rmIdx = new HashSet<Integer>(Arrays.asList(5,6));
        sheet.removeMergedRegions(rmIdx);
        rmIdx = new HashSet<Integer>(Arrays.asList(1,3));
        sheet.removeMergedRegions(rmIdx);
        assertEquals("A1:B2", ctWorksheet.getMergeCells().getMergeCellArray(0).getRef());
        assertEquals("E5:F6", ctWorksheet.getMergeCells().getMergeCellArray(1).getRef());
    }

    @Test
    public void setDefaultColumnStyle() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CTWorksheet ctWorksheet = sheet.getCTWorksheet();
        StylesTable stylesTable = workbook.getStylesSource();
        XSSFFont font = new XSSFFont();
        font.setFontName("Cambria");
        stylesTable.putFont(font);
        CTXf cellStyleXf = CTXf.Factory.newInstance();
        cellStyleXf.setFontId(1);
        cellStyleXf.setFillId(0);
        cellStyleXf.setBorderId(0);
        cellStyleXf.setNumFmtId(0);
        stylesTable.putCellStyleXf(cellStyleXf);
        CTXf cellXf = CTXf.Factory.newInstance();
        cellXf.setXfId(1);
        stylesTable.putCellXf(cellXf);
        XSSFCellStyle cellStyle = new XSSFCellStyle(1, 1, stylesTable, null);
        assertEquals(1, cellStyle.getFontIndex());

        sheet.setDefaultColumnStyle(3, cellStyle);
        assertEquals(1, ctWorksheet.getColsArray(0).getColArray(0).getStyle());
    }


    @Test
    @SuppressWarnings("deprecation")
    public void groupUngroupColumn() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        //one level
        sheet.groupColumn(2, 7);
        sheet.groupColumn(10, 11);
        CTCols cols = sheet.getCTWorksheet().getColsArray(0);
        assertEquals(2, cols.sizeOfColArray());
        CTCol[] colArray = cols.getColArray();
        assertNotNull(colArray);
        assertEquals(2 + 1, colArray[0].getMin()); // 1 based
        assertEquals(7 + 1, colArray[0].getMax()); // 1 based
        assertEquals(1, colArray[0].getOutlineLevel());
        assertEquals(0, sheet.getColumnOutlineLevel(0));

        //two level
        sheet.groupColumn(1, 2);
        cols = sheet.getCTWorksheet().getColsArray(0);
        assertEquals(4, cols.sizeOfColArray());
        colArray = cols.getColArray();
        assertEquals(2, colArray[1].getOutlineLevel());

        //three level
        sheet.groupColumn(6, 8);
        sheet.groupColumn(2, 3);
        cols = sheet.getCTWorksheet().getColsArray(0);
        assertEquals(7, cols.sizeOfColArray());
        colArray = cols.getColArray();
        assertEquals(3, colArray[1].getOutlineLevel());
        assertEquals(3, sheet.getCTWorksheet().getSheetFormatPr().getOutlineLevelCol());

        sheet.ungroupColumn(8, 10);
        colArray = cols.getColArray();
        //assertEquals(3, colArray[1].getOutlineLevel());

        sheet.ungroupColumn(4, 6);
        sheet.ungroupColumn(2, 2);
        colArray = cols.getColArray();
        assertEquals(4, colArray.length);
        assertEquals(2, sheet.getCTWorksheet().getSheetFormatPr().getOutlineLevelCol());
    }

    @Test
    public void groupUngroupRow() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        //one level
        sheet.groupRow(9, 10);
        assertEquals(2, sheet.getPhysicalNumberOfRows());
        CTRow ctrow = sheet.getRow(9).getCTRow();

        assertNotNull(ctrow);
        assertEquals(10, ctrow.getR());
        assertEquals(1, ctrow.getOutlineLevel());
        assertEquals(1, sheet.getCTWorksheet().getSheetFormatPr().getOutlineLevelRow());

        //two level
        sheet.groupRow(10, 13);
        assertEquals(5, sheet.getPhysicalNumberOfRows());
        ctrow = sheet.getRow(10).getCTRow();
        assertNotNull(ctrow);
        assertEquals(11, ctrow.getR());
        assertEquals(2, ctrow.getOutlineLevel());
        assertEquals(2, sheet.getCTWorksheet().getSheetFormatPr().getOutlineLevelRow());


        sheet.ungroupRow(8, 10);
        assertEquals(4, sheet.getPhysicalNumberOfRows());
        assertEquals(1, sheet.getCTWorksheet().getSheetFormatPr().getOutlineLevelRow());

        sheet.ungroupRow(10, 10);
        assertEquals(3, sheet.getPhysicalNumberOfRows());

        assertEquals(1, sheet.getCTWorksheet().getSheetFormatPr().getOutlineLevelRow());
    }

    @Test
    public void setZoom() {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet1 = workBook.createSheet("new sheet");
        sheet1.setZoom(3, 4);   // 75 percent magnification
        long zoom = sheet1.getCTWorksheet().getSheetViews().getSheetViewArray(0).getZoomScale();
        assertEquals(zoom, 75);

        sheet1.setZoom(200);
        zoom = sheet1.getCTWorksheet().getSheetViews().getSheetViewArray(0).getZoomScale();
        assertEquals(zoom, 200);

        try {
            sheet1.setZoom(500);
            fail("Expecting exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Valid scale values range from 10 to 400", e.getMessage());
        }
    }

    /**
     * TODO - while this is internally consistent, I'm not
     *  completely clear in all cases what it's supposed to
     *  be doing... Someone who understands the goals a little
     *  better should really review this!
     */
    @Test
    public void setColumnGroupCollapsed(){
        Workbook wb = new XSSFWorkbook();
        XSSFSheet sheet1 =(XSSFSheet) wb.createSheet();

        CTCols cols=sheet1.getCTWorksheet().getColsArray(0);
        assertEquals(0,cols.sizeOfColArray());

        sheet1.groupColumn( (short)4, (short)7 );
        sheet1.groupColumn( (short)9, (short)12 );

        assertEquals(2,cols.sizeOfColArray());

        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true, cols.getColArray(1).isSetCollapsed());
        assertEquals(10, cols.getColArray(1).getMin()); // 1 based
        assertEquals(13, cols.getColArray(1).getMax()); // 1 based

        sheet1.groupColumn( (short)10, (short)11 );
        assertEquals(4,cols.sizeOfColArray());

        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true, cols.getColArray(1).isSetCollapsed());
        assertEquals(10, cols.getColArray(1).getMin()); // 1 based
        assertEquals(10, cols.getColArray(1).getMax()); // 1 based
        assertEquals(false,cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(11, cols.getColArray(2).getMin()); // 1 based
        assertEquals(12, cols.getColArray(2).getMax()); // 1 based
        assertEquals(false,cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(13, cols.getColArray(3).getMin()); // 1 based
        assertEquals(13, cols.getColArray(3).getMax()); // 1 based

        // collapse columns - 1
        sheet1.setColumnGroupCollapsed( (short)5, true );
        assertEquals(5,cols.sizeOfColArray());

        assertEquals(true, cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true, cols.getColArray(1).isSetCollapsed());
        assertEquals(9, cols.getColArray(1).getMin()); // 1 based
        assertEquals(9, cols.getColArray(1).getMax()); // 1 based
        assertEquals(false,cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(10, cols.getColArray(2).getMin()); // 1 based
        assertEquals(10, cols.getColArray(2).getMax()); // 1 based
        assertEquals(false,cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(11, cols.getColArray(3).getMin()); // 1 based
        assertEquals(12, cols.getColArray(3).getMax()); // 1 based
        assertEquals(false,cols.getColArray(4).isSetHidden());
        assertEquals(true, cols.getColArray(4).isSetCollapsed());
        assertEquals(13, cols.getColArray(4).getMin()); // 1 based
        assertEquals(13, cols.getColArray(4).getMax()); // 1 based


        // expand columns - 1
        sheet1.setColumnGroupCollapsed( (short)5, false );

        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(false,cols.getColArray(1).isSetCollapsed());
        assertEquals(9, cols.getColArray(1).getMin()); // 1 based
        assertEquals(9, cols.getColArray(1).getMax()); // 1 based
        assertEquals(false,cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(10, cols.getColArray(2).getMin()); // 1 based
        assertEquals(10, cols.getColArray(2).getMax()); // 1 based
        assertEquals(false,cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(11, cols.getColArray(3).getMin()); // 1 based
        assertEquals(12, cols.getColArray(3).getMax()); // 1 based
        assertEquals(false,cols.getColArray(4).isSetHidden());
        assertEquals(true, cols.getColArray(4).isSetCollapsed());
        assertEquals(13, cols.getColArray(4).getMin()); // 1 based
        assertEquals(13, cols.getColArray(4).getMax()); // 1 based


        //collapse - 2
        sheet1.setColumnGroupCollapsed( (short)9, true );
        assertEquals(6,cols.sizeOfColArray());
        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true,cols.getColArray(1).isSetCollapsed());
        assertEquals(9, cols.getColArray(1).getMin()); // 1 based
        assertEquals(9, cols.getColArray(1).getMax()); // 1 based
        assertEquals(true, cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(10, cols.getColArray(2).getMin()); // 1 based
        assertEquals(10, cols.getColArray(2).getMax()); // 1 based
        assertEquals(true, cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(11, cols.getColArray(3).getMin()); // 1 based
        assertEquals(12, cols.getColArray(3).getMax()); // 1 based
        assertEquals(true, cols.getColArray(4).isSetHidden());
        assertEquals(true, cols.getColArray(4).isSetCollapsed());
        assertEquals(13, cols.getColArray(4).getMin()); // 1 based
        assertEquals(13, cols.getColArray(4).getMax()); // 1 based
        assertEquals(false,cols.getColArray(5).isSetHidden());
        assertEquals(true, cols.getColArray(5).isSetCollapsed());
        assertEquals(14, cols.getColArray(5).getMin()); // 1 based
        assertEquals(14, cols.getColArray(5).getMax()); // 1 based


        //expand - 2
        sheet1.setColumnGroupCollapsed( (short)9, false );
        assertEquals(6,cols.sizeOfColArray());
        assertEquals(14,cols.getColArray(5).getMin());

        //outline level 2: the line under ==> collapsed==True
        assertEquals(2,cols.getColArray(3).getOutlineLevel());
        assertEquals(true,cols.getColArray(4).isSetCollapsed());

        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true,cols.getColArray(1).isSetCollapsed());
        assertEquals(9, cols.getColArray(1).getMin()); // 1 based
        assertEquals(9, cols.getColArray(1).getMax()); // 1 based
        assertEquals(false,cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(10, cols.getColArray(2).getMin()); // 1 based
        assertEquals(10, cols.getColArray(2).getMax()); // 1 based
        assertEquals(true, cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(11, cols.getColArray(3).getMin()); // 1 based
        assertEquals(12, cols.getColArray(3).getMax()); // 1 based
        assertEquals(false,cols.getColArray(4).isSetHidden());
        assertEquals(true, cols.getColArray(4).isSetCollapsed());
        assertEquals(13, cols.getColArray(4).getMin()); // 1 based
        assertEquals(13, cols.getColArray(4).getMax()); // 1 based
        assertEquals(false,cols.getColArray(5).isSetHidden());
        assertEquals(false,cols.getColArray(5).isSetCollapsed());
        assertEquals(14, cols.getColArray(5).getMin()); // 1 based
        assertEquals(14, cols.getColArray(5).getMax()); // 1 based

        //DOCUMENTARE MEGLIO IL DISCORSO DEL LIVELLO
        //collapse - 3
        sheet1.setColumnGroupCollapsed( (short)10, true );
        assertEquals(6,cols.sizeOfColArray());
        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true,cols.getColArray(1).isSetCollapsed());
        assertEquals(9, cols.getColArray(1).getMin()); // 1 based
        assertEquals(9, cols.getColArray(1).getMax()); // 1 based
        assertEquals(false,cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(10, cols.getColArray(2).getMin()); // 1 based
        assertEquals(10, cols.getColArray(2).getMax()); // 1 based
        assertEquals(true, cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(11, cols.getColArray(3).getMin()); // 1 based
        assertEquals(12, cols.getColArray(3).getMax()); // 1 based
        assertEquals(false,cols.getColArray(4).isSetHidden());
        assertEquals(true, cols.getColArray(4).isSetCollapsed());
        assertEquals(13, cols.getColArray(4).getMin()); // 1 based
        assertEquals(13, cols.getColArray(4).getMax()); // 1 based
        assertEquals(false,cols.getColArray(5).isSetHidden());
        assertEquals(false,cols.getColArray(5).isSetCollapsed());
        assertEquals(14, cols.getColArray(5).getMin()); // 1 based
        assertEquals(14, cols.getColArray(5).getMax()); // 1 based


        //expand - 3
        sheet1.setColumnGroupCollapsed( (short)10, false );
        assertEquals(6,cols.sizeOfColArray());
        assertEquals(false,cols.getColArray(0).getHidden());
        assertEquals(false,cols.getColArray(5).getHidden());
        assertEquals(false,cols.getColArray(4).isSetCollapsed());

//      write out and give back
        // Save and re-load
        wb = XSSFTestDataSamples.writeOutAndReadBack(wb);
        sheet1 = (XSSFSheet)wb.getSheetAt(0);
        assertEquals(6,cols.sizeOfColArray());

        assertEquals(false,cols.getColArray(0).isSetHidden());
        assertEquals(true, cols.getColArray(0).isSetCollapsed());
        assertEquals(5, cols.getColArray(0).getMin()); // 1 based
        assertEquals(8, cols.getColArray(0).getMax()); // 1 based
        assertEquals(false,cols.getColArray(1).isSetHidden());
        assertEquals(true,cols.getColArray(1).isSetCollapsed());
        assertEquals(9, cols.getColArray(1).getMin()); // 1 based
        assertEquals(9, cols.getColArray(1).getMax()); // 1 based
        assertEquals(false,cols.getColArray(2).isSetHidden());
        assertEquals(true, cols.getColArray(2).isSetCollapsed());
        assertEquals(10, cols.getColArray(2).getMin()); // 1 based
        assertEquals(10, cols.getColArray(2).getMax()); // 1 based
        assertEquals(false,cols.getColArray(3).isSetHidden());
        assertEquals(true, cols.getColArray(3).isSetCollapsed());
        assertEquals(11, cols.getColArray(3).getMin()); // 1 based
        assertEquals(12, cols.getColArray(3).getMax()); // 1 based
        assertEquals(false,cols.getColArray(4).isSetHidden());
        assertEquals(false,cols.getColArray(4).isSetCollapsed());
        assertEquals(13, cols.getColArray(4).getMin()); // 1 based
        assertEquals(13, cols.getColArray(4).getMax()); // 1 based
        assertEquals(false,cols.getColArray(5).isSetHidden());
        assertEquals(false,cols.getColArray(5).isSetCollapsed());
        assertEquals(14, cols.getColArray(5).getMin()); // 1 based
        assertEquals(14, cols.getColArray(5).getMax()); // 1 based
    }

    /**
     * TODO - while this is internally consistent, I'm not
     *  completely clear in all cases what it's supposed to
     *  be doing... Someone who understands the goals a little
     *  better should really review this!
     */
    @Test
    public void setRowGroupCollapsed(){
        Workbook wb = new XSSFWorkbook();
        XSSFSheet sheet1 = (XSSFSheet)wb.createSheet();

        sheet1.groupRow( 5, 14 );
        sheet1.groupRow( 7, 14 );
        sheet1.groupRow( 16, 19 );

        assertEquals(14,sheet1.getPhysicalNumberOfRows());
        assertEquals(false,sheet1.getRow(6).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(6).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(7).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(7).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(9).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(9).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(14).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(14).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetHidden());

        //collapsed
        sheet1.setRowGroupCollapsed( 7, true );

        assertEquals(false,sheet1.getRow(6).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(6).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(7).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(7).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(9).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(9).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(14).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(14).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetHidden());

        //expanded
        sheet1.setRowGroupCollapsed( 7, false );

        assertEquals(false,sheet1.getRow(6).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(6).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(7).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(7).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(9).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(9).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(14).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(14).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetHidden());


        // Save and re-load
        wb = XSSFTestDataSamples.writeOutAndReadBack(wb);
        sheet1 = (XSSFSheet)wb.getSheetAt(0);

        assertEquals(false,sheet1.getRow(6).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(6).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(7).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(7).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(9).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(9).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(14).getCTRow().isSetCollapsed());
        assertEquals(true, sheet1.getRow(14).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(16).getCTRow().isSetHidden());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetCollapsed());
        assertEquals(false,sheet1.getRow(18).getCTRow().isSetHidden());
    }

    /**
     * Get / Set column width and check the actual values of the underlying XML beans
     */
    @Test
    public void columnWidth_lowlevel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        sheet.setColumnWidth(1, 22 * 256);
        assertEquals(22 * 256, sheet.getColumnWidth(1));

        // Now check the low level stuff, and check that's all
        //  been set correctly
        XSSFSheet xs = sheet;
        CTWorksheet cts = xs.getCTWorksheet();

        assertEquals(1, cts.sizeOfColsArray());
        CTCols cols = cts.getColsArray(0);
        assertEquals(1, cols.sizeOfColArray());
        CTCol col = cols.getColArray(0);

        // XML is 1 based, POI is 0 based
        assertEquals(2, col.getMin());
        assertEquals(2, col.getMax());
        assertEquals(22.0, col.getWidth(), 0.0);
        assertTrue(col.getCustomWidth());

        // Now set another
        sheet.setColumnWidth(3, 33 * 256);

        assertEquals(1, cts.sizeOfColsArray());
        cols = cts.getColsArray(0);
        assertEquals(2, cols.sizeOfColArray());

        col = cols.getColArray(0);
        assertEquals(2, col.getMin()); // POI 1
        assertEquals(2, col.getMax());
        assertEquals(22.0, col.getWidth(), 0.0);
        assertTrue(col.getCustomWidth());

        col = cols.getColArray(1);
        assertEquals(4, col.getMin()); // POI 3
        assertEquals(4, col.getMax());
        assertEquals(33.0, col.getWidth(), 0.0);
        assertTrue(col.getCustomWidth());
    }

    /**
     * Setting width of a column included in a column span
     */
    @Test
    public void bug47862() {
        XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("47862.xlsx");
        XSSFSheet sheet = wb.getSheetAt(0);
        CTCols cols = sheet.getCTWorksheet().getColsArray(0);
        //<cols>
        //  <col min="1" max="5" width="15.77734375" customWidth="1"/>
        //</cols>

        //a span of columns [1,5]
        assertEquals(1, cols.sizeOfColArray());
        CTCol col = cols.getColArray(0);
        assertEquals(1, col.getMin());
        assertEquals(5, col.getMax());
        double swidth = 15.77734375; //width of columns in the span
        assertEquals(swidth, col.getWidth(), 0.0);

        for (int i = 0; i < 5; i++) {
            assertEquals((int)(swidth*256), sheet.getColumnWidth(i));
        }

        int[] cw = new int[]{10, 15, 20, 25, 30};
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, cw[i]*256);
        }

        //the check below failed prior to fix of Bug #47862
        ColumnHelper.sortColumns(cols);
        //<cols>
        //  <col min="1" max="1" customWidth="true" width="10.0" />
        //  <col min="2" max="2" customWidth="true" width="15.0" />
        //  <col min="3" max="3" customWidth="true" width="20.0" />
        //  <col min="4" max="4" customWidth="true" width="25.0" />
        //  <col min="5" max="5" customWidth="true" width="30.0" />
        //</cols>

        //now the span is splitted into 5 individual columns
        assertEquals(5, cols.sizeOfColArray());
        for (int i = 0; i < 5; i++) {
            assertEquals(cw[i]*256, sheet.getColumnWidth(i));
            assertEquals(cw[i], cols.getColArray(i).getWidth(), 0.0);
        }

        //serialize and check again
        wb = XSSFTestDataSamples.writeOutAndReadBack(wb);
        sheet = wb.getSheetAt(0);
        cols = sheet.getCTWorksheet().getColsArray(0);
        assertEquals(5, cols.sizeOfColArray());
        for (int i = 0; i < 5; i++) {
            assertEquals(cw[i]*256, sheet.getColumnWidth(i));
            assertEquals(cw[i], cols.getColArray(i).getWidth(), 0.0);
        }
    }

    /**
     * Hiding a column included in a column span
     */
    @Test
    public void bug47804() {
        XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("47804.xlsx");
        XSSFSheet sheet = wb.getSheetAt(0);
        CTCols cols = sheet.getCTWorksheet().getColsArray(0);
        assertEquals(2, cols.sizeOfColArray());
        CTCol col;
        //<cols>
        //  <col min="2" max="4" width="12" customWidth="1"/>
        //  <col min="7" max="7" width="10.85546875" customWidth="1"/>
        //</cols>

        //a span of columns [2,4]
        col = cols.getColArray(0);
        assertEquals(2, col.getMin());
        assertEquals(4, col.getMax());
        //individual column
        col = cols.getColArray(1);
        assertEquals(7, col.getMin());
        assertEquals(7, col.getMax());

        sheet.setColumnHidden(2, true); // Column C
        sheet.setColumnHidden(6, true); // Column G

        assertTrue(sheet.isColumnHidden(2));
        assertTrue(sheet.isColumnHidden(6));

        //other columns but C and G are not hidden
        assertFalse(sheet.isColumnHidden(1));
        assertFalse(sheet.isColumnHidden(3));
        assertFalse(sheet.isColumnHidden(4));
        assertFalse(sheet.isColumnHidden(5));

        //the check below failed prior to fix of Bug #47804
        ColumnHelper.sortColumns(cols);
        //the span is now splitted into three parts
        //<cols>
        //  <col min="2" max="2" customWidth="true" width="12.0" />
        //  <col min="3" max="3" customWidth="true" width="12.0" hidden="true"/>
        //  <col min="4" max="4" customWidth="true" width="12.0"/>
        //  <col min="7" max="7" customWidth="true" width="10.85546875" hidden="true"/>
        //</cols>

        assertEquals(4, cols.sizeOfColArray());
        col = cols.getColArray(0);
        assertEquals(2, col.getMin());
        assertEquals(2, col.getMax());
        col = cols.getColArray(1);
        assertEquals(3, col.getMin());
        assertEquals(3, col.getMax());
        col = cols.getColArray(2);
        assertEquals(4, col.getMin());
        assertEquals(4, col.getMax());
        col = cols.getColArray(3);
        assertEquals(7, col.getMin());
        assertEquals(7, col.getMax());

        //serialize and check again
        wb = XSSFTestDataSamples.writeOutAndReadBack(wb);
        sheet = wb.getSheetAt(0);
        assertTrue(sheet.isColumnHidden(2));
        assertTrue(sheet.isColumnHidden(6));
        assertFalse(sheet.isColumnHidden(1));
        assertFalse(sheet.isColumnHidden(3));
        assertFalse(sheet.isColumnHidden(4));
        assertFalse(sheet.isColumnHidden(5));
    }

    @Test
    public void commentsTable() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet();
        CommentsTable comment1 = sheet1.getCommentsTable(false);
        assertNull(comment1);

        comment1 = sheet1.getCommentsTable(true);
        assertNotNull(comment1);
        assertEquals("/xl/comments1.xml", comment1.getPackageRelationship().getTargetURI().toString());

        assertSame(comment1, sheet1.getCommentsTable(true));

        //second sheet
        XSSFSheet sheet2 = workbook.createSheet();
        CommentsTable comment2 = sheet2.getCommentsTable(false);
        assertNull(comment2);

        comment2 = sheet2.getCommentsTable(true);
        assertNotNull(comment2);

        assertSame(comment2, sheet2.getCommentsTable(true));
        assertEquals("/xl/comments2.xml", comment2.getPackageRelationship().getTargetURI().toString());

        //comment1 and  comment2 are different objects
        assertNotSame(comment1, comment2);

        //now test against a workbook containing cell comments
        workbook = XSSFTestDataSamples.openSampleWorkbook("WithMoreVariousData.xlsx");
        sheet1 = workbook.getSheetAt(0);
        comment1 = sheet1.getCommentsTable(true);
        assertNotNull(comment1);
        assertEquals("/xl/comments1.xml", comment1.getPackageRelationship().getTargetURI().toString());
        assertSame(comment1, sheet1.getCommentsTable(true));
    }

    /**
     * Rows and cells can be created in random order,
     * but CTRows are kept in ascending order
     */
    @Override
    @Test
    @SuppressWarnings("deprecation")
    public void createRow() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CTWorksheet wsh = sheet.getCTWorksheet();
        CTSheetData sheetData = wsh.getSheetData();
        assertEquals(0, sheetData.sizeOfRowArray());

        XSSFRow row1 = sheet.createRow(2);
        row1.createCell(2);
        row1.createCell(1);

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(2);
        row2.createCell(1);
        row2.createCell(0);

        XSSFRow row3 = sheet.createRow(0);
        row3.createCell(3);
        row3.createCell(0);
        row3.createCell(2);
        row3.createCell(5);


        CTRow[] xrow = sheetData.getRowArray();
        assertEquals(3, xrow.length);

        //rows are sorted: {0, 1, 2}
        assertEquals(4, xrow[0].sizeOfCArray());
        assertEquals(1, xrow[0].getR());
        assertTrue(xrow[0].equals(row3.getCTRow()));

        assertEquals(3, xrow[1].sizeOfCArray());
        assertEquals(2, xrow[1].getR());
        assertTrue(xrow[1].equals(row2.getCTRow()));

        assertEquals(2, xrow[2].sizeOfCArray());
        assertEquals(3, xrow[2].getR());
        assertTrue(xrow[2].equals(row1.getCTRow()));

        CTCell[] xcell = xrow[0].getCArray();
        assertEquals("D1", xcell[0].getR());
        assertEquals("A1", xcell[1].getR());
        assertEquals("C1", xcell[2].getR());
        assertEquals("F1", xcell[3].getR());

        //re-creating a row does NOT add extra data to the parent
        row2 = sheet.createRow(1);
        assertEquals(3, sheetData.sizeOfRowArray());
        //existing cells are invalidated
        assertEquals(0, sheetData.getRowArray(1).sizeOfCArray());
        assertEquals(0, row2.getPhysicalNumberOfCells());

        workbook = XSSFTestDataSamples.writeOutAndReadBack(workbook);
        sheet = workbook.getSheetAt(0);
        wsh = sheet.getCTWorksheet();
        xrow = sheetData.getRowArray();
        assertEquals(3, xrow.length);

        //rows are sorted: {0, 1, 2}
        assertEquals(4, xrow[0].sizeOfCArray());
        assertEquals(1, xrow[0].getR());
        //cells are now sorted
        xcell = xrow[0].getCArray();
        assertEquals("A1", xcell[0].getR());
        assertEquals("C1", xcell[1].getR());
        assertEquals("D1", xcell[2].getR());
        assertEquals("F1", xcell[3].getR());


        assertEquals(0, xrow[1].sizeOfCArray());
        assertEquals(2, xrow[1].getR());

        assertEquals(2, xrow[2].sizeOfCArray());
        assertEquals(3, xrow[2].getR());

    }

    @Test
    public void createRowAfterLastRow() {
        createRowAfterLastRow(SpreadsheetVersion.EXCEL2007);
    }

    @Test
    public void setAutoFilter() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("new sheet");
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:D100"));

        assertEquals("A1:D100", sheet.getCTWorksheet().getAutoFilter().getRef());

        // auto-filter must be registered in workboook.xml, see Bugzilla 50315
        XSSFName nm = wb.getBuiltInName(XSSFName.BUILTIN_FILTER_DB, 0);
        assertNotNull(nm);

        assertEquals(0, nm.getCTName().getLocalSheetId());
        assertEquals(true, nm.getCTName().getHidden());
        assertEquals("_xlnm._FilterDatabase", nm.getCTName().getName());
        assertEquals("'new sheet'!$A$1:$D$100", nm.getCTName().getStringValue());
    }

    @Test
    public void protectSheet_lowlevel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        CTSheetProtection pr = sheet.getCTWorksheet().getSheetProtection();
        assertNull("CTSheetProtection should be null by default", pr);
        String password = "Test";
        sheet.protectSheet(password);
        pr = sheet.getCTWorksheet().getSheetProtection();
        assertNotNull("CTSheetProtection should be not null", pr);
        assertTrue("sheet protection should be on", pr.isSetSheet());
        assertTrue("object protection should be on", pr.isSetObjects());
        assertTrue("scenario protection should be on", pr.isSetScenarios());
        int hashVal = CryptoFunctions.createXorVerifier1(password);
        int actualVal = Integer.parseInt(pr.xgetPassword().getStringValue(),16);
        assertEquals("well known value for top secret hash should match", hashVal, actualVal);

        sheet.protectSheet(null);
        assertNull("protectSheet(null) should unset CTSheetProtection", sheet.getCTWorksheet().getSheetProtection());
    }

    @Test
    public void protectSheet_lowlevel_2013() {
        String password = "test";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet xs = wb.createSheet();
        xs.setSheetPassword(password, HashAlgorithm.sha384);
        wb = writeOutAndReadBack(wb);
        assertTrue(wb.getSheetAt(0).validateSheetPassword(password));

        wb = openSampleWorkbook("workbookProtection-sheet_password-2013.xlsx");
        assertTrue(wb.getSheetAt(0).validateSheetPassword("pwd"));
    }


    @Test
    public void bug49966() {
        XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("49966.xlsx");
        CalculationChain calcChain = wb.getCalculationChain();
        assertNotNull(wb.getCalculationChain());
        assertEquals(3, calcChain.getCTCalcChain().sizeOfCArray());

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);

        sheet.removeRow(row);
        assertEquals("XSSFSheet#removeRow did not clear calcChain entries",
                0, calcChain.getCTCalcChain().sizeOfCArray());

        //calcChain should be gone
        wb = XSSFTestDataSamples.writeOutAndReadBack(wb);
        assertNull(wb.getCalculationChain());

    }

    /**
     * See bug #50829
     */
    @Test
    public void tables() {
       XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("WithTable.xlsx");
       assertEquals(3, wb.getNumberOfSheets());

       // Check the table sheet
       XSSFSheet s1 = wb.getSheetAt(0);
       assertEquals("a", s1.getRow(0).getCell(0).getRichStringCellValue().toString());
       assertEquals(1.0, s1.getRow(1).getCell(0).getNumericCellValue(), 0);

       List<XSSFTable> tables = s1.getTables();
       assertNotNull(tables);
       assertEquals(1, tables.size());

       XSSFTable table = tables.get(0);
       assertEquals("Tabella1", table.getName());
       assertEquals("Tabella1", table.getDisplayName());

       // And the others
       XSSFSheet s2 = wb.getSheetAt(1);
       assertEquals(0, s2.getTables().size());
       XSSFSheet s3 = wb.getSheetAt(2);
       assertEquals(0, s3.getTables().size());
    }

    /**
     * Test to trigger OOXML-LITE generating to include org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr
     */
    @Test
    public void setForceFormulaRecalculation() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");

        assertFalse(sheet.getForceFormulaRecalculation());

        // Set
        sheet.setForceFormulaRecalculation(true);
        assertEquals(true, sheet.getForceFormulaRecalculation());

        // calcMode="manual" is unset when forceFormulaRecalculation=true
        CTCalcPr calcPr = workbook.getCTWorkbook().addNewCalcPr();
        calcPr.setCalcMode(STCalcMode.MANUAL);
        sheet.setForceFormulaRecalculation(true);
        assertEquals(STCalcMode.AUTO, calcPr.getCalcMode());

        // Check
        sheet.setForceFormulaRecalculation(false);
        assertEquals(false, sheet.getForceFormulaRecalculation());


        // Save, re-load, and re-check
        workbook = XSSFTestDataSamples.writeOutAndReadBack(workbook);
        sheet = workbook.getSheet("Sheet 1");
        assertEquals(false, sheet.getForceFormulaRecalculation());
    }

    @Test
    public void bug54607() throws IOException {
        // run with the file provided in the Bug-Report
        runGetTopRow("54607.xlsx", true, 1, 0, 0);
        runGetLeftCol("54607.xlsx", true, 0, 0, 0);

        // run with some other flie to see
        runGetTopRow("54436.xlsx", true, 0);
        runGetLeftCol("54436.xlsx", true, 0);
        runGetTopRow("TwoSheetsNoneHidden.xlsx", true, 0, 0);
        runGetLeftCol("TwoSheetsNoneHidden.xlsx", true, 0, 0);
        runGetTopRow("TwoSheetsNoneHidden.xls", false, 0, 0);
        runGetLeftCol("TwoSheetsNoneHidden.xls", false, 0, 0);
    }

    private void runGetTopRow(String file, boolean isXSSF, int... topRows) throws IOException {
        final Workbook wb;
        if(isXSSF) {
            wb = XSSFTestDataSamples.openSampleWorkbook(file);
        } else {
            wb = HSSFTestDataSamples.openSampleWorkbook(file);
        }
        for (int si = 0; si < wb.getNumberOfSheets(); si++) {
            Sheet sh = wb.getSheetAt(si);
            assertNotNull(sh.getSheetName());
            assertEquals("Did not match for sheet " + si, topRows[si], sh.getTopRow());
        }

        // for XSSF also test with SXSSF
        if(isXSSF) {
            Workbook swb = new SXSSFWorkbook((XSSFWorkbook) wb);
            try {
                for (int si = 0; si < swb.getNumberOfSheets(); si++) {
                    Sheet sh = swb.getSheetAt(si);
                    assertNotNull(sh.getSheetName());
                    assertEquals("Did not match for sheet " + si, topRows[si], sh.getTopRow());
                }
            } finally {
                swb.close();
            }
        }
    }

    private void runGetLeftCol(String file, boolean isXSSF, int... topRows) throws IOException {
        final Workbook wb;
        if(isXSSF) {
            wb = XSSFTestDataSamples.openSampleWorkbook(file);
        } else {
            wb = HSSFTestDataSamples.openSampleWorkbook(file);
        }
        for (int si = 0; si < wb.getNumberOfSheets(); si++) {
            Sheet sh = wb.getSheetAt(si);
            assertNotNull(sh.getSheetName());
            assertEquals("Did not match for sheet " + si, topRows[si], sh.getLeftCol());
        }

        // for XSSF also test with SXSSF
        if(isXSSF) {
            Workbook swb = new SXSSFWorkbook((XSSFWorkbook) wb);
            for (int si = 0; si < swb.getNumberOfSheets(); si++) {
                Sheet sh = swb.getSheetAt(si);
                assertNotNull(sh.getSheetName());
                assertEquals("Did not match for sheet " + si, topRows[si], sh.getLeftCol());
            }
            swb.close();
        }
    }

    @Test
    public void bug55745() throws Exception {
        XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("55745.xlsx");
        XSSFSheet sheet = wb.getSheetAt(0);
        List<XSSFTable> tables = sheet.getTables();
        /*System.out.println(tables.size());

        for(XSSFTable table : tables) {
            System.out.println("XPath: " + table.getCommonXpath());
            System.out.println("Name: " + table.getName());
            System.out.println("Mapped Cols: " + table.getNumerOfMappedColumns());
            System.out.println("Rowcount: " + table.getRowCount());
            System.out.println("End Cell: " + table.getEndCellReference());
            System.out.println("Start Cell: " + table.getStartCellReference());
        }*/
        assertEquals("Sheet should contain 8 tables", 8, tables.size());
        assertNotNull("Sheet should contain a comments table", sheet.getCommentsTable(false));
    }

    @Test
    public void bug55723b(){
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();

        // stored with a special name
        assertNull(wb.getBuiltInName(XSSFName.BUILTIN_FILTER_DB, 0));

        CellRangeAddress range = CellRangeAddress.valueOf("A:B");
        AutoFilter filter = sheet.setAutoFilter(range);
        assertNotNull(filter);

        // stored with a special name
        XSSFName name = wb.getBuiltInName(XSSFName.BUILTIN_FILTER_DB, 0);
        assertNotNull(name);
        assertEquals("Sheet0!$A:$B", name.getRefersToFormula());

        range = CellRangeAddress.valueOf("B:C");
        filter = sheet.setAutoFilter(range);
        assertNotNull(filter);

        // stored with a special name
        name = wb.getBuiltInName(XSSFName.BUILTIN_FILTER_DB, 0);
        assertNotNull(name);
        assertEquals("Sheet0!$B:$C", name.getRefersToFormula());
    }

    @Test(timeout=180000)
    public void bug51585(){
        XSSFTestDataSamples.openSampleWorkbook("51585.xlsx");
    }

    private XSSFWorkbook setupSheet(){
        //set up workbook
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        Row row1 = sheet.createRow((short) 0);
        Cell cell = row1.createCell((short) 0);
        cell.setCellValue("Names");
        Cell cell2 = row1.createCell((short) 1);
        cell2.setCellValue("#");

        Row row2 = sheet.createRow((short) 1);
        Cell cell3 = row2.createCell((short) 0);
        cell3.setCellValue("Jane");
        Cell cell4 = row2.createCell((short) 1);
        cell4.setCellValue(3);

        Row row3 = sheet.createRow((short) 2);
        Cell cell5 = row3.createCell((short) 0);
        cell5.setCellValue("John");
        Cell cell6 = row3.createCell((short) 1);
        cell6.setCellValue(3);

        return wb;
    }

    @Test
    public void testCreateTwoPivotTablesInOneSheet(){
        XSSFWorkbook wb = setupSheet();
        XSSFSheet sheet = wb.getSheetAt(0);

        assertNotNull(wb);
        assertNotNull(sheet);
        XSSFPivotTable pivotTable = sheet.createPivotTable(new AreaReference("A1:B2"), new CellReference("H5"));
        assertNotNull(pivotTable);
        assertTrue(wb.getPivotTables().size() > 0);
        XSSFPivotTable pivotTable2 = sheet.createPivotTable(new AreaReference("A1:B2"), new CellReference("L5"), sheet);
        assertNotNull(pivotTable2);
        assertTrue(wb.getPivotTables().size() > 1);
    }

    @Test
    public void testCreateTwoPivotTablesInTwoSheets(){
        XSSFWorkbook wb = setupSheet();
        XSSFSheet sheet = wb.getSheetAt(0);

        assertNotNull(wb);
        assertNotNull(sheet);
        XSSFPivotTable pivotTable = sheet.createPivotTable(new AreaReference("A1:B2"), new CellReference("H5"));
        assertNotNull(pivotTable);
        assertTrue(wb.getPivotTables().size() > 0);
        assertNotNull(wb);
        XSSFSheet sheet2 = wb.createSheet();
        XSSFPivotTable pivotTable2 = sheet2.createPivotTable(new AreaReference("A1:B2"), new CellReference("H5"), sheet);
        assertNotNull(pivotTable2);
        assertTrue(wb.getPivotTables().size() > 1);
    }

    @Test
    public void testCreatePivotTable(){
        XSSFWorkbook wb = setupSheet();
        XSSFSheet sheet = wb.getSheetAt(0);

        assertNotNull(wb);
        assertNotNull(sheet);
        XSSFPivotTable pivotTable = sheet.createPivotTable(new AreaReference("A1:B2"), new CellReference("H5"));
        assertNotNull(pivotTable);
        assertTrue(wb.getPivotTables().size() > 0);
    }

    @Test
    public void testCreatePivotTableInOtherSheetThanDataSheet(){
        XSSFWorkbook wb = setupSheet();
        XSSFSheet sheet1 = wb.getSheetAt(0);
        XSSFSheet sheet2 = wb.createSheet();

        XSSFPivotTable pivotTable = sheet2.createPivotTable
                (new AreaReference("A1:B2"), new CellReference("H5"), sheet1);
        assertEquals(0, pivotTable.getRowLabelColumns().size());

        assertEquals(1, wb.getPivotTables().size());
        assertEquals(0, sheet1.getPivotTables().size());
        assertEquals(1, sheet2.getPivotTables().size());
    }

    @Test
    public void testCreatePivotTableInOtherSheetThanDataSheetUsingAreaReference(){
        XSSFWorkbook wb = setupSheet();
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFSheet sheet2 = wb.createSheet();

        XSSFPivotTable pivotTable = sheet2.createPivotTable
                (new AreaReference(sheet.getSheetName()+"!A$1:B$2"), new CellReference("H5"));
        assertEquals(0, pivotTable.getRowLabelColumns().size());
    }

    @Test
    public void testCreatePivotTableWithConflictingDataSheets(){
        XSSFWorkbook wb = setupSheet();
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFSheet sheet2 = wb.createSheet();

        try {
            sheet2.createPivotTable(new AreaReference(sheet.getSheetName()+"!A$1:B$2"), new CellReference("H5"), sheet2);
        } catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testReadFails() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        try {
            sheet.onDocumentRead();
            fail("Throws exception because we cannot read here");
        } catch (POIXMLException e) {
            // expected here
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateComment() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        assertNotNull(sheet.createComment());
    }
}