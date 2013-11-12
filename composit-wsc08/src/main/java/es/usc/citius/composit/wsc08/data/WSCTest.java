package es.usc.citius.composit.wsc08.data;

import es.usc.citius.composit.core.model.Signature;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.core.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.ZipException;


public enum WSCTest {


    TESTSET_2008_01(Ref.file, "TESTSET/Set01MetaData/", Ref.request2008_01, new int[]{16, 12, 7, 10, 3, 4, 1, 1, 1, 5}),
    TESTSET_2008_02(Ref.file, "TESTSET/Set02MetaData/", Ref.request2008_02, new int[]{9, 15, 11, 16, 5, 4, 1, 1}),
    TESTSET_2008_03(Ref.file, "TESTSET/Set03MetaData/", Ref.request2008_03, new int[]{4, 2, 1, 3, 6, 5, 2, 4, 4, 4, 5, 9, 10, 2, 2, 15, 5, 1, 2, 2, 8, 6, 3}),
    TESTSET_2008_04(Ref.file, "TESTSET/Set04MetaData/", Ref.request2008_04, new int[]{15, 9, 10, 7, 3}),
    TESTSET_2008_05(Ref.file, "TESTSET/Set05MetaData/", Ref.request2008_05, new int[]{11, 14, 12, 17, 9, 12, 13, 9, 4, 1}),
    TESTSET_2008_06(Ref.file, "TESTSET/Set06MetaData/", Ref.request2008_06, new int[]{43, 15, 47, 20, 31, 24, 9, 2, 6, 2, 1, 2, 1, 1}),
    TESTSET_2008_07(Ref.file, "TESTSET/Set07MetaData/", Ref.request2008_07, new int[]{6, 18, 15, 12, 7, 6, 4, 9, 13, 15, 8, 11, 10, 17, 9, 3, 1}),
    TESTSET_2008_08(Ref.file, "TESTSET/Set08MetaData/", Ref.request2008_08, new int[]{11, 5, 4, 6, 5, 9, 3, 2, 10, 7, 7, 3, 5, 4, 2, 4, 12, 3, 4, 15, 5, 1, 3, 1});

    private static class Ref {
        private static final String file = "wsc08-testsets.zip";
        private static Signature request2008_01 = new SignatureIO(Arrays.asList("con1233457844", "con1849951292", "con864995873"), Arrays.asList("con1220759822", "con2119691623"));
        private static Signature request2008_02 = new SignatureIO(Arrays.asList("con1498435960", "con189054683", "con608925131", "con1518098260"), Arrays.asList("con357002459"));
        private static Signature request2008_03 = new SignatureIO(Arrays.asList("con1765781068", "con1958306700", "con1881706184"), Arrays.asList("con896546722"));
        private static Signature request2008_04 = new SignatureIO(Arrays.asList("con1174477567", "con1559120783", "con34478529", "con1492759465", "con1735261165", "con1801416348"), Arrays.asList("con1183284356", "con1112009574", "con102886439", "con1157274091"));
        private static Signature request2008_05 = new SignatureIO(Arrays.asList("con428391640", "con2100909192"), Arrays.asList("con1092196197", "con1374634550", "con2055848680"));
        private static Signature request2008_06 = new SignatureIO(Arrays.asList("con1927582736", "con2066855250", "con1482928259", "con1174685776", "con1929429507", "con1036639498", "con683948594", "con1055275345", "con74623429"), Arrays.asList("con2139158577", "con1855489728", "con1888258136", "con1299621724"));
        private static Signature request2008_07 = new SignatureIO(Arrays.asList("con484707919", "con797253905", "con891470167", "con1591526279", "con1250100988", "con2073456634", "con222954059", "con409949952"), Arrays.asList("con477743641"));
        private static Signature request2008_08 = new SignatureIO(Arrays.asList("con1269728670", "con1666449411", "con917858046", "con625786234", "con1966097554"), Arrays.asList("con817293857", "con900031981", "con991999738", "con1395691071"));

    }



    private String testFile, testPath;
    // Default dataset request
    private Signature request;
    private int[] expected;

    WSCTest(String testFile, String testPath, Signature request, int[] expected) {
        this.testFile = absolutePath(testFile);
        this.testPath = testPath;
        this.request = request;
        this.expected = expected;
    }

    private InputStream openStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    private String absolutePath(String path) {
        String file = getClass().getClassLoader().getResource(path).getFile();
        return file;
    }

    public InputStream openServicesStream() throws ZipException,
            IOException {
        File zipFile = new File(testFile);
        return FileUtils.openZipEntry(zipFile, testPath + "services.xml");
    }

    public InputStream openTaxonomyStream() throws ZipException,
            IOException {
        File zipFile = new File(testFile);
        return FileUtils.openZipEntry(zipFile, testPath + "taxonomy.xml");
    }

    public WSCXMLServideProvider createResourceProvider() throws ZipException,
            IOException {
        return new WSCXMLServideProvider(openServicesStream());
    }

    public int[] getExpected() {
        return expected;
    }
}
