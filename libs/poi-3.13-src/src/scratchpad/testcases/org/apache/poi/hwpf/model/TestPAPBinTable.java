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

package org.apache.poi.hwpf.model;

import java.io.ByteArrayOutputStream;
import java.util.List;

import junit.framework.TestCase;

import org.apache.poi.hwpf.HWPFDocFixture;
import org.apache.poi.hwpf.HWPFTestDataSamples;
import org.apache.poi.hwpf.model.io.HWPFFileSystem;

public final class TestPAPBinTable extends TestCase
{

    public void testObIs()
    {
        // shall not fail with assertions on
        HWPFTestDataSamples.openSampleFile( "ob_is.doc" );
    }

    public void testReadWrite() throws Exception
    {
        /** @todo verify the constructors */
        HWPFDocFixture _hWPFDocFixture = new HWPFDocFixture( this,
                HWPFDocFixture.DEFAULT_TEST_FILE );

        _hWPFDocFixture.setUp();
        TextPieceTable fakeTPT = new TextPieceTable();

        FileInformationBlock fib = _hWPFDocFixture._fib;
        byte[] mainStream = _hWPFDocFixture._mainStream;
        byte[] tableStream = _hWPFDocFixture._tableStream;

        PAPBinTable _pAPBinTable = new PAPBinTable( mainStream, tableStream,
                null, fib.getFcPlcfbtePapx(), fib.getLcbPlcfbtePapx(), fakeTPT );

        HWPFFileSystem fileSys = new HWPFFileSystem();

        _pAPBinTable.writeTo( fileSys, fakeTPT );
        ByteArrayOutputStream tableOut = fileSys.getStream( "1Table" );
        ByteArrayOutputStream mainOut = fileSys.getStream( "WordDocument" );

        byte[] newTableStream = tableOut.toByteArray();
        byte[] newMainStream = mainOut.toByteArray();

        PAPBinTable newBinTable = new PAPBinTable( newMainStream,
                newTableStream, null, 0, newTableStream.length, fakeTPT );

        List<PAPX> oldTextRuns = _pAPBinTable.getParagraphs();
        List<PAPX> newTextRuns = newBinTable.getParagraphs();

        assertEquals( oldTextRuns.size(), newTextRuns.size() );

        int size = oldTextRuns.size();
        for ( int x = 0; x < size; x++ )
        {
            PAPX oldNode = oldTextRuns.get( x );
            PAPX newNode = newTextRuns.get( x );

            assertTrue( oldNode.equals( newNode ) );
        }

        _hWPFDocFixture.tearDown();
    }
}
