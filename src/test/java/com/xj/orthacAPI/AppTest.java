package com.xj.orthacAPI;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void getPatientInfos()
    {
        OrthancTool   ot=new OrthancTool();
        List<String> infos=ot.getPatientInfos();
        for (String info : infos) {
            System.out.println(info);
        }
    }


    @Test
    public void getStudies()
    {
        OrthancTool   ot=new OrthancTool();
        List<String> infos=ot.getStudies();
        for (String info : infos) {
            System.out.println(info);
        }
    }


    @Test
    public void getInfos()
    {
        OrthancTool   ot=new OrthancTool();
        //List<String> infos=ot.getStudies();
        //List<String> infos=ot.getSeries();
        List<String> infos=ot.getInstances();

        for (String info : infos) {
            System.out.println(info);
        }
    }



    @Test
    public void getPatient()
    {
        OrthancTool   ot=new OrthancTool();
        ot.getPatient("c9b0ff25-29a5faeb-269fa5e7-a3e9c99f-794ed755");
    }


//下载dicom
    @Test
    public void downDicomInstance()
    {
        OrthancTool   ot=new OrthancTool();
        ot.downInstance("7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8","d://temp//test555.dcm");
    }


    //下载dicom
    @Test
    public void downDicomInstancePng()
    {
        OrthancTool   ot=new OrthancTool();
        ot.downInstancePng("7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8","d://temp//test444.png");
    }


    @Test
    public void getSimpleTag()
    {
        OrthancTool   ot=new OrthancTool();
        ot.getSimpleTag("7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8");
    }


    @Test
    public void getTag()
    {
        OrthancTool   ot=new OrthancTool();
        ot.getTag("7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8");
    }


    //    访问实例的原始DICOM字段

    @Test
    public void getPatientNameByTag()
    {
        OrthancTool   ot=new OrthancTool();
        ot.getDicomField("7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8","0010-0010");
    }


    @Test
    public void getAllTagByInstanceId()
    {
        OrthancTool   ot=new OrthancTool();
        ot.getAllTag("7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8");
    }






}
