/*
 * Copyright (c) 2018 Lisandro Fernandez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.lisandro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.lisandro.model.*;

public class App {

    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        try {
            printModel();
            createData();
            removeData();
            updateData();
            System.out.println("");
            printModel();
        } catch (Exception e) {
            throw e;
        } finally {
            emf.close();
        }
    }

    private static void printModel() {
        printProducts();
        printCustomers();
        printOrders();
        printItems();
    }

    private static void printProducts() {
        EntityManager manager = emf.createEntityManager();
        List<Product> products = manager.createQuery("Select p FROM Product p", Product.class).getResultList();
        System.out.println("*** Products ***");
        for (Product product : products)
            System.out.println(product.getClass() + " ["
                    + "id: " + product.getId() + ", "
                    + "sku: " + product.getSku() + ", "
                    + "name: " + product.getName() + ", "
                    + "price: " + product.getPrice() + ", "
                    + "version: " + product.getVersion() + ", "
                    + "creation datetime: " + product.getCreationDateTime() + ", "
                    + "modification datetime: " + product.getModificationDateTime() + "]");
        System.out.println("Product count: " + products.size());
        manager.close();
    }

    private static void printCustomers() {
        EntityManager manager = emf.createEntityManager();
        List<Customer> customers = manager.createQuery("Select c FROM Customer c", Customer.class).getResultList();
        System.out.println("*** Customers ***");
        for (Customer customer : customers)
            System.out.println(customer.getClass() + " ["
                    + "id: " + customer.getId() + ", "
                    + "name: " + customer.getName() + ", "
                    + "birth date: " + customer.getBirthDate() + ", "
                    + "address: " + customer.getAddress() + ", "
                    + "version: " + customer.getVersion() + ", "
                    + "creation datetime: " + customer.getCreationDateTime() + ", "
                    + "modification datetime: " + customer.getModificationDateTime() + "]");
        System.out.println("Customer count: " + customers.size());
        manager.close();
    }

    private static void printOrders() {
        System.out.println("*** Orders ***");
        EntityManager manager = emf.createEntityManager();
        List<PurchaseOrder> orders = manager.createQuery("Select o FROM PurchaseOrder o", PurchaseOrder.class).getResultList();
        for (PurchaseOrder order : orders)
            System.out.println(order.getClass() + " ["
                    + "id: " + order.getId() + ", "
                    + "customer: " + order.getCustomer() + ", "
                    + "order date: " + order.getOrderDate() + ", "
                    + "version: " + order.getVersion() + ", "
                    + "creation datetime: " + order.getCreationDateTime() + ", "
                    + "modification datetime: " + order.getModificationDateTime() + "]");
        System.out.println("Order count: " + orders.size());
        manager.close();
    }

    private static void printItems() {
        System.out.println("*** Items ***");
        EntityManager manager = emf.createEntityManager();
        List<Item> items = manager.createQuery("Select i FROM Item i", Item.class).getResultList();
        for (Item item : items)
            System.out.println(item.getClass() + " ["
                    + "id: " + item.getId() + ", "
                    + "product: " + item.getProduct() + ", "
                    + "order: " + item.getOrder() + ", "
                    + "quantity: " + item.getQuantity() + ", "
                    + "total: " + item.getTotal() + ", "
                    + "version: " + item.getVersion() + ", "
                    + "creation datetime: " + item.getCreationDateTime() + ", "
                    + "modification datetime: " + item.getModificationDateTime() + "]");
        System.out.println("Item count: " + items.size());
        manager.close();
    }

    private static void createData() {
        EntityManager manager = emf.createEntityManager();
        try {
            manager.getTransaction().begin();
            // create product
            Product product = new Product();
            product.setSku("les-paul");
            product.setName("Guitar Gibson Les Paul");
            product.setPrice(new BigDecimal("2495.39"));
            manager.persist(product);
            // create customer
            Customer customer = new Customer();
            customer.setName("Saul Hudson");
            customer.setBirthDate(LocalDate.of(1965, 7, 23));
            manager.persist(customer);
            // create address
            Address address = new Address();
            address.setCustomer(customer);
            address.setStreet("10350 Santa Monica Blvd.");
            address.setPostalCode("CA 90025-5075");
            manager.persist(address);
            // create e-mail address
            EmailAddress email = new EmailAddress();
            email.setAddress("slash@gunsnroses.com");
            email.setCustomer(customer);
            manager.persist(email);
            // create order
            PurchaseOrder order = new PurchaseOrder();
            order.setCustomer(customer);
            order.setOrderDate(LocalDate.now());
            manager.persist(order);
            // create items
            Item item1 = new Item();
            item1.setOrder(order);
            item1.setProduct(product);
            item1.setQuantity(2);
            item1.setTotal(new BigDecimal("4990.78"));
            manager.persist(item1);
            Item item2 = new Item();
            item2.setOrder(order);
            item2.setProduct(manager.createQuery("Select p FROM Product p where p.sku = :sku",
                                                 Product.class)
                                    .setParameter("sku", "bed")
                                    .getSingleResult());
            item2.setQuantity(1);
            item2.setTotal(new BigDecimal("131.00"));
            manager.persist(item2);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        } finally {
            manager.close();
        }
    }

    private static void removeData() {
        EntityManager manager = emf.createEntityManager();
        try {
            Item item = manager.find(Item.class, 5L);
            manager.getTransaction().begin();
            manager.remove(item);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        } finally {
            manager.close();
        }
    }

    private static void updateData() {
        EntityManager manager = emf.createEntityManager();
        try {
            PurchaseOrder order = manager.find(PurchaseOrder.class, 3L);
            manager.getTransaction().begin();
            order.setOrderDate(LocalDate.of(2018, 2, 21));
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        } finally {
            manager.close();
        }
    }

}
