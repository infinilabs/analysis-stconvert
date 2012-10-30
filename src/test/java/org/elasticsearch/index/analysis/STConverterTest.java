package org.elasticsearch.index.analysis;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * User: Medcl
 * Date: 12-10-28
 * Time: 上午8:44
 */
public class STConverterTest extends TestCase {
    public void testConvert() throws Exception {

        STConverter stConverter=new STConverter();
        String str= stConverter.convert(ConvertType.traditional2simple,"先禮後兵");
        String str1= stConverter.convert(ConvertType.simple2traditional,"非诚勿扰");
        System.out.printf(str);
        System.out.println();
        System.out.printf(str1);
        Assert.assertEquals("先礼后兵",str);
        Assert.assertEquals("非誠勿擾",str1);
    }
}
