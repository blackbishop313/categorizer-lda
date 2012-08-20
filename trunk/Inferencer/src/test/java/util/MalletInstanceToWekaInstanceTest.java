package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import weka.core.Attribute;
import weka.core.Instances;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class MalletInstanceToWekaInstanceTest {
    private Logger logger = Logger.getLogger(MalletInstanceToWekaInstance.class);

    @Mock
    private Instance malletInstance;
    private MalletInstanceToWekaInstance instanceAdapter;


//    @Test
//    public void testGetCategories(){
//        CategoriesGetter categoriesGetter =new CategoriesGetter();
//        InstanceDecoder decoder = mock(InstanceDecoder.class);
//        categoriesGetter.decoder =decoder;
//        InstanceList instanceList = new InstanceList()
//        Instance instance = mock(Instance.class);
//        int times = 10;
//        for (int i=0;i< times;i++){
//            instanceList.add(instance);
//        }
//        when(decoder.getCategory(any(Instance.class))).thenReturn("a");
//        Collection<String> ret =categoriesGetter.getCategories(instanceList);
//        assertEquals(ret.iterator().next(), "a");
//        assertEquals(ret.size(),1);
//        verify(decoder,times(times)).getCategory(instance);
//
//    }


    @Before
    public void setUp(){
        instanceAdapter = new MalletInstanceToWekaInstance();
    }

    /**
     * No better way to test this  due to weka
     */
    @Test
    public void testCreateWekaInstance(){
        String text = "text";
        String[] cats= {"a","b","c"};
        Collection<String> categories = Arrays.asList(cats);
        Instances instances = instanceAdapter.createWekaInstances(categories,text,"test");
        Attribute attribute = instances.attribute(text);
        InstanceDecoder decoder = mock(InstanceDecoder.class);
        instanceAdapter.decoder = decoder;
        malletInstance = mock(Instance.class);
        when(decoder.getCategory(malletInstance)).thenReturn("a");
        when(decoder.getTextAsFeatures(malletInstance)).thenReturn("1 2 3 4");

        weka.core.Instance wekaInstance =instanceAdapter.create(attribute, malletInstance, instances);
        String message = wekaInstance.toString();
        logger.debug(message);
        junit.framework.Assert.assertEquals(message,"'1 2 3 4',a");
    }

    public void testInsertInstance(){
        Instances instances = mock(Instances.class);
        weka.core.Instance wekaInstance = mock(weka.core.Instance.class);
        instanceAdapter.insert(wekaInstance,instances);
        verify(wekaInstance).setDataset(instances);
        verify(instances).add(wekaInstance);
    }
}
