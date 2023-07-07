package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getFactory;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        SessionFactory factory = getFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "name VARCHAR(30) NOT NULL, " +
                    "lastName VARCHAR(30) NOT NULL, " +
                    "age TINYINT(3) NOT NULL)").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        SessionFactory factory = getFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        SessionFactory factory = getFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
            session.beginTransaction();
            session.persist(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        SessionFactory factory = getFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        SessionFactory factory = getFactory();
        Session session = factory.getCurrentSession();
        List<User> users = new ArrayList<>();

        try (factory; session) {
            session.beginTransaction();
            users = session.createQuery("FROM User", User.class).list();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        SessionFactory factory = getFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
            session.beginTransaction();
            session.createQuery("DELETE from User").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
        }
    }
}