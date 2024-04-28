package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.*;
import com.Back.PorscheDashboardBackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class GroupService {
    @Autowired

    private HourlyKosuService hourlyKosuService;
    @Autowired
    private SerialDao serialDao;
    @Autowired
private ProjectDao projectDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private ShiftDao shiftDao;
    @Autowired
    private HourlyKosuDao hourlyKosuDao;



    public Groupe createNewGroup (Groupe groupe){
        return groupDao.save(groupe);
    }

    public void InitGroup() {
        Project GT4RS=new Project();

        GT4RS.setProjectName("GT4RS");
        projectDao.save(GT4RS);

        Project P992=new Project();

        P992.setProjectName("992");
        projectDao.save(P992);

        Project B4T=new Project();

        B4T.setProjectName("B4T");
        projectDao.save(B4T);

        Project p9A3=new Project();

        p9A3.setProjectName("9A3");
        projectDao.save(p9A3);

        Project B6S=new Project();

        B6S.setProjectName("B6S");
        projectDao.save(B6S);

        Groupe GP11 =new Groupe();
        GP11.setGroupName("G 011-1");
        groupDao.save(GP11);


        Groupe GP12 =new Groupe();
        GP12.setGroupName("G 011-2");
        groupDao.save(GP12);

        Groupe GP13 =new Groupe();
        GP13.setGroupName("G 011-3");
        groupDao.save(GP13);



     // B4T SERIALS

        Serial serial1 = new Serial();
        serial1.setCoefficient(0.93);
        serial1.setSerialName("9A2.607.055.05");
        serial1.setProject(B4T);
        serialDao.save(serial1);

        Serial serial2 = new Serial();
        serial2.setCoefficient(0.94);
        serial2.setSerialName("9A2.607.056.00");
        serial2.setProject(B4T);
        serialDao.save(serial2);

        Serial serial3 = new Serial();
        serial3.setCoefficient(1);
        serial3.setSerialName("9A2.607.057.00");
        serial3.setProject(B4T);
        serialDao.save(serial3);

        Serial serial4 = new Serial();
        serial4.setCoefficient(0.94);
        serial4.setSerialName("9A2.607.060.05");
        serial4.setProject(B4T);
        serialDao.save(serial4);

        Serial serial5 = new Serial();
        serial5.setCoefficient(0.94);
        serial5.setSerialName("9A2.607.061.00");
        serial5.setProject(B4T);
        serialDao.save(serial5);

        Serial serial6 = new Serial();
        serial6.setCoefficient(1);
        serial6.setSerialName("9A2.607.062.00");
        serial6.setProject(B4T);
        serialDao.save(serial6);

        Serial S1=new Serial();
        S1.setCoefficient(1);
        S1.setSerialName("982.971.595.C");
        S1.setProject(GT4RS);
        serialDao.save(S1);

        Serial S2=new Serial();
        S2.setCoefficient(1);

        S2.setSerialName("582.123.595.B");
        S2.setProject(GT4RS);
        serialDao.save(S2);

        Serial S3=new Serial();
        S3.setCoefficient(1);

        S3.setSerialName("582.123.595.Test1");
        S3.setProject(P992);
        serialDao.save(S3);

        Serial S4=new Serial();
        S4.setCoefficient(1);

        S4.setSerialName("582.123.595.Test2");
        S4.setProject(P992);
        serialDao.save(S4);

        Serial S9=new Serial();
        S9.setCoefficient(1);
        S9.setSerialName(" B6S: 582.123.595.Tester");
        S9.setProject(B6S);
        serialDao.save(S9);

        Serial S8=new Serial();
        S8.setCoefficient(1);
        S8.setSerialName(" 9A3: 582.123.595.A3");
        S8.setProject(B6S);
        serialDao.save(S8);


        Serial S10=new Serial();
        S10.setCoefficient(1);
        S10.setSerialName(" B6S: 582.123.595.SSSS");
        S9.setProject(B6S);
        serialDao.save(S10);



        Line L1=new Line();
        L1.setLineName("Ligne 1");
        L1.setProject(P992);
        lineDao.save(L1);


        Line L2=new Line();
        L2.setLineName("Ligne 2");
        L2.setProject(B6S);
        lineDao.save(L2);

        Line L3=new Line();
        L3.setLineName("Ligne 3");
        L3.setProject(B4T);
        lineDao.save(L3);

        Line L4=new Line();
        L4.setLineName("Ligne 4");
        L4.setProject(GT4RS);
        lineDao.save(L4);














    }

    public List<Groupe> getAllGroupes() {
        return (List<Groupe>) groupDao.findAll();
    }

    public void deleteGroupe(Long groupId) {
        groupDao.deleteById(groupId);
    }
    public Groupe updateGroupe(Long groupId, Groupe updatedGroup) {
        Groupe existingGroup = groupDao.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        existingGroup.setGroupName(updatedGroup.getGroupName());

        return groupDao.save(existingGroup);
    }
}
