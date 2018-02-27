package edu.eci.cosw.samples;


import edu.eci.cosw.jpa.sample.model.Consulta;
import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.persistence.PatientsRepository;
import edu.eci.cosw.samples.services.PatientServices;
import edu.eci.cosw.samples.services.ServicesException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApiApplication.class)
@WebAppConfiguration
@ActiveProfiles("**test**")
public class SpringDataRestApiApplicationTests {
    
    @Autowired
    private PatientServices ps;
    
    @Autowired
    private PatientsRepository pr;
        
        
    @Test
    public void findPacienteAlreadyExist(){
        Set<Consulta> c = new HashSet<>();
        c.add(new Consulta(new Date(), "query"));
        Paciente p = new Paciente(new PacienteId(1, "cc"), "Pipe", new Date(),c);
        pr.save(p);
        try {
            assert  p.getNombre().equals(ps.getPatient(1, "cc").getNombre());
        } catch (ServicesException ex) {
            fail("Fail in test findPacienteAlreadyExist");
        }finally{
            pr.deleteAll();
        }
    }
        
    @Test
    public void dontHaveQueryPacienteThatExist(){
        try {
            ps.getPatient(1, "cc");
            fail("Fail in test dontHaveQueryPacienteThatExist");
        } catch (ServicesException ex) {        
            }finally{
                pr.deleteAll();
            }
        }
    
    @Test
    public void donthaveGetPacienteWithoutLessQuery(){
        Set<Consulta> c = new HashSet<>();
        c.add(new Consulta(new Date(), "query"));
        Paciente p = new Paciente(new PacienteId(1, "cc"), "Pipe", new Date(),c);
        pr.save(p);
        try {
            List al=ps.topPatients(2);
            assert al.isEmpty();
        } catch (ServicesException ex) {
            fail("Fail in test donthaveGetPacienteWithoutLessQuery");
        }finally{
            pr.deleteAll();
        }
    }
    
    @Test
    public void haveQueryPacienteWithNQuery(){
        Set<Consulta> c = new HashSet<>();
        Set<Consulta> c2 = new HashSet<>();
        Set<Consulta> c3 = new HashSet<>();
        c.add(new Consulta(new Date(), "query"));c.add(new Consulta(new Date(), "query2"));
        c2.add(new Consulta(new Date(), "query3"));
        Paciente p = new Paciente(new PacienteId(1, "cc"), "Pipe", new Date(),c);
        Paciente p2 = new Paciente(new PacienteId(1, "cc"), "Pipe", new Date(),c2);
        Paciente p3 = new Paciente(new PacienteId(1, "cc"), "Pipe", new Date(),c3);
        pr.save(p);
        pr.save(p2);
        pr.save(p3);
        try {
            List al=ps.topPatients(2);
            assert al.size()==1;
        } catch (ServicesException ex) {
            fail("Fail in test haveQueryPacienteWithNQuery");
        }finally{
            pr.deleteAll();
        }
     }
}
