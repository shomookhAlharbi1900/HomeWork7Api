package org.example.Controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dao.EmployeeDAO;
import org.example.dto.EmployeeDto;
import org.example.dto.employeeFilterDto;
import org.example.exceptions.DataNotFoundException;
import org.example.models.employees;


import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
@Path("/employees")
public class employessController {
    EmployeeDAO em = new EmployeeDAO();
    @Context
    UriInfo uriInfo;
    @Context
    HttpHeaders headers;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response selectAllEmployees(@BeanParam employeeFilterDto filter) {
        try {
            GenericEntity<ArrayList<employees>> job = new GenericEntity<ArrayList<employees>>(em.selectAllEmployees(filter)) {};
            if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                return Response
                        .ok(job)
                        .type(MediaType.APPLICATION_XML)
                        .build();
            }
            return Response
                    .ok(job, MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @Path("{employee_id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response selectEmployee(@PathParam("employee_id") int employee_id)throws SQLException {
        try {
            employees emp = em.selectEmployee(employee_id);
            if (emp == null) {
                throw new DataNotFoundException(" Employee " + employee_id + " Not Found!!");
            }

            EmployeeDto dto = new EmployeeDto();
            dto.setEmployee_id(emp.getEmployee_id());
            dto.setFirst_name(emp.getFirst_name());
            dto.setLast_name(emp.getLast_name());
            dto.setEmail(emp.getEmail());
            dto.setPhone_number(emp.getPhone_number());
            dto.setHire_date(emp.getHire_date());
            dto.setJob_id(emp.getJob_id());
            dto.setSalary(emp.getSalary());
            dto.setManager_id(emp.getManager_id());
            dto.setDepartment_id(emp.getDepartment_id());
            addLinks(dto);

            if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                return Response
                        .ok(dto)
                        .type(MediaType.APPLICATION_XML)
                        .build();
            }

            return Response
                    .ok(dto, MediaType.APPLICATION_JSON)
                    .build();

        } catch (ClassNotFoundException  e) {
            throw new RuntimeException(e);
        }
    }


    @DELETE
    @Path("{employee_id}")
    public Response deleteEmployee(@PathParam("employee_id") int employee_id) {

        try {
            em.deleteEmployee(employee_id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @POST
    public Response insertEmployee(employees emp) {
        try {
            em.insertEmployee(emp);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PUT
    @Path("{employee_id}")
    public Response updateEmployee(@PathParam("employee_id") int employee_id, employees emp) {

        try {
            emp.setEmployee_id(employee_id);
            em.updateEmployee(emp);
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addLinks (EmployeeDto dto){
        URI selfUri = uriInfo.getAbsolutePath();
        URI empsUri = uriInfo.getAbsolutePathBuilder().path(employessController.class).build();

        dto.addLink(selfUri.toString(), "self");
//        dto.addLink(empsUri.toString(),"emp");
    }

//    @GET
//    @Path("{job_id}")
//    public Response select_Emp_job_id(@PathParam("job_id") int job_id) {
//        try {
//            if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
//            return Response
//                    .ok(em)
//                    .type(MediaType.APPLICATION_XML)
//                    .build();
//        }
//            return Response
//                    .ok(em, MediaType.APPLICATION_JSON)
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @GET
//    @Path("{hire_date}")
//    public Response select_Emp_hire_date(@PathParam("hire_date") String hire_date) {
//        try {
//            if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
//                return Response
//                        .ok(em)
//                        .type(MediaType.APPLICATION_XML)
//                        .build();
//            }
//
//            return Response
//                    .ok(em, MediaType.APPLICATION_JSON)
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }













}

