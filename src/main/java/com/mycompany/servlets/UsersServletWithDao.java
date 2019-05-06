package com.mycompany.servlets;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.mycompany.dao.UsersDao;
import com.mycompany.dao.UsersDaoJdbcImpl;
import com.mycompany.dao.UsersDaoJdbcTemplateImpl;
import com.mycompany.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@WebServlet("/users")
public class UsersServletWithDao extends HttpServlet {

    private UsersDao usersDao;

    @Override
    public void init() throws ServletException {
        Properties properties = new Properties();
        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();

        try {
            properties.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/db.properties")));
            String dbUrl = properties.getProperty("db.url");
            String dbUsername = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.password");
            String driverClassName = properties.getProperty("db.driverClassName");

            dataSource.setUsername(dbUsername);
            dataSource.setPassword(dbPassword);
            dataSource.setUrl(dbUrl);
            dataSource.setDriverClassName(driverClassName);

             usersDao = new UsersDaoJdbcImpl(dataSource);
            //usersDao = new UsersDaoJdbcTemplateImpl(dataSource);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Optional<User> user = usersDao.find(2);
        int i = 0;

        List<User> users = null;
        if (req.getParameter("firstName") != null) {
            String firstName = req.getParameter("firstName");
            users = usersDao.findAllByFirstName(firstName);
        } else {
            users = usersDao.findAll();
        }
        req.setAttribute("usersFromServer", users);
        req.getServletContext().getRequestDispatcher("/jsp/users.jsp").forward(req, resp);
    }

}
