package com.example.api

import com.example.api.endpoint.*
import com.example.api.handler.*
import com.example.dto.*
import com.example.service.{ DepartmentService, EmployeePhoneService, EmployeeService, PhoneService }
import zio.*
import zio.http.*
import zio.http.endpoint.openapi.*

trait Router
    extends DepartmentEndpoints
    with DepartmentHandlers
    with EmployeeEndpoints
    with EmployeeHandlers
    with PhoneEndpoints
    with PhoneHandlers
    with EmployeePhoneEndpoints
    with EmployeePhoneHandlers:
  val routes: Routes[DepartmentService & EmployeeService & PhoneService & EmployeePhoneService, Nothing] =
    Routes(
      createDepartment.implementHandler(handler(createDepartmentHandler)),
      getDepartments.implementHandler(handler(getDepartmentsHandler)),
      getDepartmentById.implementHandler(handler(getDepartmentHandler)),
      updateDepartment.implementHandler(handler(updateDepartmentHandler)),
      deleteDepartment.implementHandler(handler(deleteDepartmentHandler)),
      createEmployee.implementHandler(handler(createEmployeeHandler)),
      getEmployees.implementHandler(handler(getEmployeesHandler)),
      getEmployeeById.implementHandler(handler(getEmployeeHandler)),
      updateEmployee.implementHandler(handler(updateEmployeeHandler)),
      deleteEmployee.implementHandler(handler(deleteEmployeeHandler)),
      createPhone.implementHandler(handler(createPhoneHandler)),
      getPhoneById.implementHandler(handler(getPhoneHandler)),
      updatePhone.implementHandler(handler(updatePhoneHandler)),
      deletePhone.implementHandler(handler(deletePhoneHandler)),
      addPhoneToEmployee.implementHandler(handler(addPhoneToEmployeeHandler)),
      retrieveEmployeePhones.implementHandler(handler(retrieveEmployeePhonesHandler)),
      removePhoneFromEmployee.implementHandler(handler(removePhoneFromEmployeeHandler))
    )

  val swaggerRoutes =
    SwaggerUI.routes(
      "docs",
      OpenAPIGen.fromEndpoints(
        createDepartment,
        getDepartments,
        getDepartmentById,
        updateDepartment,
        deleteDepartment,
        createEmployee,
        getEmployees,
        getEmployeeById,
        updateEmployee,
        deleteEmployee,
        createPhone,
        getPhoneById,
        updatePhone,
        deletePhone,
        addPhoneToEmployee,
        retrieveEmployeePhones,
        removePhoneFromEmployee
      )
    )
