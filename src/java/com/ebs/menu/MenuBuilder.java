package com.ebs.menu;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eflores on 06/04/2017.
 */
public class MenuBuilder {
    private Document doc;
    private WebMenu menu;
    private String xml;

    public MenuBuilder(String xmlMenuFile) {
        try {
            xml = xmlMenuFile;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public WebMenu getFullMenu() {
        menu = new WebMenu();
        List<SubMenu> menus;
        List<MenuItem> items;
        List<Option> opts;
        int level = 2;//Root level is 1, initial children level is 2
        NodeList nodesMenu = doc.getElementsByTagName("subMenu");
        NodeList nodesEntry = doc.getElementsByTagName("menuEntry");
        NodeList nodesOther = doc.getElementsByTagName("Opcion");
        menus = new ArrayList<SubMenu>();
        items = new ArrayList<MenuItem>();
        opts = new ArrayList<Option>();
        //Submenus
        for (int i = 0; i < nodesMenu.getLength(); i++) {
            Element el = (Element) nodesMenu.item(i);
            if (Integer.parseInt(el.getAttribute("nivel")) == level) {
                SubMenu subm = getSubmenu(el, level + 1);//menu parent
                subm.setIcon(el.getAttribute("icon"));
                subm.setName(el.getAttribute("name"));
                subm.setTitle(el.getAttribute("title"));
                subm.setNivel(Integer.parseInt(el.getAttribute("nivel")));
                subm.setPosition(Integer.parseInt(el.getAttribute("position")));
                if (el.hasAttribute("perfil"))
                    subm.setPerfilValue(Long.parseLong(el.getAttribute("perfil")));
                menus.add(subm);
            }
        }
        //Otros
        getOptAdList(opts, nodesOther);
        for (int i = 0; i < nodesEntry.getLength(); i++) {
            //TODO Load nodes entry information
            //TODO add to list of entries
            MenuItem mItm = new MenuItem();
        }
        menu.setItems(items);
        menu.setMenus(menus);
        menu.setOptions(opts);
        return menu;
    }

    public WebMenu getMenuAsList() {
        menu = new WebMenu();
        List<SubMenu> menus;
        List<Option> opts;
        int level = 2;//Root level is 1, initial children level is 2
        NodeList nodesMenu = doc.getElementsByTagName("subMenu");
        NodeList nodesOther = doc.getElementsByTagName("Opcion");
        menus = new ArrayList<SubMenu>();
        opts = new ArrayList<Option>();

        //Submenus
        for (int i = 0; i < nodesMenu.getLength(); i++) {
            Element el = (Element) nodesMenu.item(i);
            SubMenu tmp = getSubMenuEntries(el);
            if (Integer.parseInt(el.getAttribute("nivel")) == level) {
                tmp.setIcon(el.getAttribute("icon"));
                tmp.setName(el.getAttribute("name"));
                tmp.setTitle(el.getAttribute("title"));
                tmp.setNivel(Integer.parseInt(el.getAttribute("nivel")));
                tmp.setPosition(Integer.parseInt(el.getAttribute("position")));
                if (el.hasAttribute("perfil"))
                    tmp.setPerfilValue(Long.parseLong(el.getAttribute("perfil")));
                menus.add(tmp);
            }
        }
        getOptAdList(opts, nodesOther);
        menu.setMenus(menus);
        menu.setOptions(opts);
        return menu;
    }

    private void getOptAdList(List<Option> opts, NodeList nodesOther) {
        //Otros
        for (int i = 0; i < nodesOther.getLength(); i++) {
            Element el = (Element) nodesOther.item(i);
            Option opt = new Option();
            opt.setName(el.getAttribute("name"));
            opt.setTitle(el.getAttribute("title"));
            opt.setPerfilValue(Long.parseLong(el.getAttribute("perfil")));
            opts.add(opt);
        }
    }

    public WebMenu getMenuByUserPerfil(Long pe) {
        menu = new WebMenu();

        return menu;
    }

    private SubMenu getSubMenuEntries(Element parent) {
        List<MenuItem> mi;
        mi = new ArrayList<MenuItem>();
        SubMenu current = new SubMenu();//Actual parent menu or submenu container
        NodeList nodesEntry = parent.getElementsByTagName("menuEntry");

        for (int j = 0; j < nodesEntry.getLength(); j++) {
            Element els = (Element) nodesEntry.item(j);
            MenuItem miTmp = new MenuItem();
            miTmp.setNivel(Integer.parseInt(els.getAttribute("nivel")));
            miTmp.setPosition(Integer.parseInt(els.getAttribute("position")));
            miTmp.setIcon(els.getAttribute("icon"));
            miTmp.setName(els.getAttribute("name"));
            miTmp.setTitle(els.getAttribute("title"));
            miTmp.setUrl(els.getAttribute("url"));
            if (els.hasAttribute("perfil")) {
                miTmp.setPerfilValue(Long.parseLong(els.getAttribute("perfil")));
                mi.add(miTmp);
            }
        }
        current.setItems(mi);
        return current;
    }

    private SubMenu getSubmenu(Element parent, int level) {
        List<SubMenu> sm;
        List<MenuItem> mi;
        sm = new ArrayList<SubMenu>();
        mi = new ArrayList<MenuItem>();

        SubMenu current = new SubMenu();//Actual parent menu or submenu container

        NodeList nodesMenu = parent.getElementsByTagName("subMenu");
        NodeList nodesEntry = parent.getElementsByTagName("menuEntry");
        //System.out.println("Nodes lenght:" + nodesEntry.getLength());
        for (int i = 0; i < nodesMenu.getLength(); i++) {
            Element el = (Element) nodesMenu.item(i);
            if (Integer.parseInt(el.getAttribute("nivel")) == level) {
                SubMenu tmp;
                tmp = getSubmenu(el, level + 1);//un submenu siempre tiene entradas
                tmp.setIcon(el.getAttribute("icon"));
                tmp.setName(el.getAttribute("name"));
                tmp.setTitle(el.getAttribute("title"));
                tmp.setNivel(Integer.parseInt(el.getAttribute("nivel")));
                tmp.setPosition(Integer.parseInt(el.getAttribute("position")));
                if (el.hasAttribute("perfil"))
                    tmp.setPerfilValue(Long.parseLong(el.getAttribute("perfil")));
                sm.add(tmp);
            }
        }

        for (int j = 0; j < nodesEntry.getLength(); j++) {
            Element els = (Element) nodesEntry.item(j);
            if (Integer.parseInt(els.getAttribute("nivel")) == level) {
                MenuItem miTmp = new MenuItem();
                miTmp.setNivel(Integer.parseInt(els.getAttribute("nivel")));
                miTmp.setPosition(Integer.parseInt(els.getAttribute("position")));
                miTmp.setIcon(els.getAttribute("icon"));
                miTmp.setName(els.getAttribute("name"));
                miTmp.setTitle(els.getAttribute("title"));
                miTmp.setUrl(els.getAttribute("url"));
                if (els.hasAttribute("perfil"))
                    miTmp.setPerfilValue(Long.parseLong(els.getAttribute("perfil")));
                mi.add(miTmp);
            }

        }
        current.setItems(mi);
        current.setMenus(sm);
        return current;
    }
}