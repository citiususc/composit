package es.usc.citius.composit.wsc08.data;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.SetMatchFunctionDecorator;
import es.usc.citius.composit.core.matcher.logic.LogicMatchType;
import es.usc.citius.composit.core.matcher.logic.LogicMatcher;
import es.usc.citius.composit.core.model.Signature;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.core.util.FileUtils;
import es.usc.citius.composit.wsc08.data.knowledge.WSCXMLKnowledgeBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;


public enum WSCTest {


    TESTSET_2008_01(Ref.resourceName, "TESTSET/Set01MetaData/", Ref.request2008_01, new int[]{16, 12, 7, 10, 3, 4, 1, 1, 1, 5}),
    TESTSET_2008_02(Ref.resourceName, "TESTSET/Set02MetaData/", Ref.request2008_02, new int[]{9, 15, 11, 16, 5, 4, 1, 1}),
    TESTSET_2008_03(Ref.resourceName, "TESTSET/Set03MetaData/", Ref.request2008_03, new int[]{4, 2, 1, 3, 6, 5, 2, 4, 4, 4, 5, 9, 10, 2, 2, 15, 5, 1, 2, 2, 8, 6, 3}),
    TESTSET_2008_04(Ref.resourceName, "TESTSET/Set04MetaData/", Ref.request2008_04, new int[]{15, 9, 10, 7, 3}),
    TESTSET_2008_05(Ref.resourceName, "TESTSET/Set05MetaData/", Ref.request2008_05, new int[]{11, 14, 12, 17, 9, 12, 13, 9, 4, 1}),
    TESTSET_2008_06(Ref.resourceName, "TESTSET/Set06MetaData/", Ref.request2008_06, new int[]{43, 15, 47, 20, 31, 24, 9, 2, 6, 2, 1, 2, 1, 1}),
    TESTSET_2008_07(Ref.resourceName, "TESTSET/Set07MetaData/", Ref.request2008_07, new int[]{6, 18, 15, 12, 7, 6, 4, 9, 13, 15, 8, 11, 10, 17, 9, 3, 1}),
    TESTSET_2008_08(Ref.resourceName, "TESTSET/Set08MetaData/", Ref.request2008_08, new int[]{11, 5, 4, 6, 5, 9, 3, 2, 10, 7, 7, 3, 5, 4, 2, 4, 12, 3, 4, 15, 5, 1, 3, 1});

    private static class Ref {
        private static final String resourceName = "wsc08-testsets.zip";
        private static SignatureIO<String> request2008_01 = new SignatureIO<String>(Arrays.asList("con1233457844", "con1849951292", "con864995873"), Arrays.asList("con1220759822", "con2119691623"));
        private static SignatureIO<String> request2008_02 = new SignatureIO<String>(Arrays.asList("con1498435960", "con189054683", "con608925131", "con1518098260"), Arrays.asList("con357002459"));
        private static SignatureIO<String> request2008_03 = new SignatureIO<String>(Arrays.asList("con1765781068", "con1958306700", "con1881706184"), Arrays.asList("con896546722"));
        private static SignatureIO<String> request2008_04 = new SignatureIO<String>(Arrays.asList("con1174477567", "con1559120783", "con34478529", "con1492759465", "con1735261165", "con1801416348"), Arrays.asList("con1183284356", "con1112009574", "con102886439", "con1157274091"));
        private static SignatureIO<String> request2008_05 = new SignatureIO<String>(Arrays.asList("con428391640", "con2100909192"), Arrays.asList("con1092196197", "con1374634550", "con2055848680"));
        private static SignatureIO<String> request2008_06 = new SignatureIO<String>(Arrays.asList("con1927582736", "con2066855250", "con1482928259", "con1174685776", "con1929429507", "con1036639498", "con683948594", "con1055275345", "con74623429"), Arrays.asList("con2139158577", "con1855489728", "con1888258136", "con1299621724"));
        private static SignatureIO<String> request2008_07 = new SignatureIO<String>(Arrays.asList("con484707919", "con797253905", "con891470167", "con1591526279", "con1250100988", "con2073456634", "con222954059", "con409949952"), Arrays.asList("con477743641"));
        private static SignatureIO<String> request2008_08 = new SignatureIO<String>(Arrays.asList("con1269728670", "con1666449411", "con917858046", "con625786234", "con1966097554"), Arrays.asList("con817293857", "con900031981", "con991999738", "con1395691071"));

    }

    public static class Dataset {
        private WSCXMLKnowledgeBase kb;
        private WSCServiceProvider serviceProvider;
        private SetMatchFunction<Concept, LogicMatchType> defaultMatcher;
        private SignatureIO<String> request;

        public Dataset(SignatureIO<String> request, WSCServiceProvider serviceProvider, WSCXMLKnowledgeBase kb, SetMatchFunction<Concept, LogicMatchType> defaultMatcher) {
            this.serviceProvider = serviceProvider;
            this.request = request;
            this.kb = kb;
            this.defaultMatcher = defaultMatcher;
        }

        public WSCXMLKnowledgeBase getKb() {
            return kb;
        }

        public WSCServiceProvider getServiceProvider() {
            return serviceProvider;
        }

        public SetMatchFunction<Concept, LogicMatchType> getDefaultMatcher() {
            return defaultMatcher;
        }

        public SignatureIO<String> getRequest() {
            return request;
        }
    }

    private String resourceName, zipPath;
    // Default dataset request
    private SignatureIO<String> request;
    private int[] expected;

    WSCTest(String resourceName, String zipPath, SignatureIO<String> request, int[] expected) {
        this.resourceName = resourceName;
        this.zipPath = zipPath;
        this.request = request;
        this.expected = expected;
    }

    private InputStream openStream(String resource) {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    public ZipInputStream openServicesStream() throws ZipException,
            IOException {
        ZipInputStream zipStream = new ZipInputStream(openStream(resourceName));
        String entryName = zipPath + "services.xml";
        return FileUtils.moveToZipEntry(zipStream, entryName);
    }

    public ZipInputStream openTaxonomyStream() throws ZipException,
            IOException {
        ZipInputStream zipStream = new ZipInputStream(openStream(resourceName));
        String entryName = zipPath + "taxonomy.xml";
        return FileUtils.moveToZipEntry(zipStream, entryName);
    }

    public WSCXMLServideProvider createXmlResourceProvider() throws ZipException,
            IOException {
        return new WSCXMLServideProvider(openServicesStream());
    }

    public WSCXMLKnowledgeBase createKnowledgeBase() throws IOException {
        return new WSCXMLKnowledgeBase(openTaxonomyStream());
    }

    public WSCServiceProvider createSemanticServiceProvider() throws IOException {
        return new WSCServiceProvider(createXmlResourceProvider(), createKnowledgeBase());
    }

    public Dataset dataset() throws IOException {
        ZipInputStream taxonomy = openTaxonomyStream();
        ZipInputStream services = openServicesStream();
        // Streams are automatically closed by JAXB
        WSCXMLKnowledgeBase kb = new WSCXMLKnowledgeBase(taxonomy);
        WSCXMLServideProvider xmlServiceProvider = new WSCXMLServideProvider(services);
        WSCServiceProvider serviceProvider = new WSCServiceProvider(xmlServiceProvider, kb);
        LogicMatcher matcher = new LogicMatcher(kb);
        SetMatchFunction<Concept, LogicMatchType> setMatcher = new SetMatchFunctionDecorator<Concept, LogicMatchType>(matcher);
        return new Dataset(request, serviceProvider, kb, setMatcher);
    }



    public int[] getExpected() {
        return expected;
    }
}
