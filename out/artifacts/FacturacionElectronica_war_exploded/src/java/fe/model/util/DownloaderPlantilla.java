/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util;

import fe.db.MPlantilla;
import fe.model.dao.PlantillaDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class DownloaderPlantilla {

    private PlantillaDAO daoPlantillas;

    public void writeStore(HttpServletRequest request, HttpServletResponse response, int id) throws Exception {
        daoPlantillas = new PlantillaDAO();
        if (request.getSession().getAttribute("macceso") == null) {
            response.getOutputStream().write("¡ No tiene sesión iniciada!, favor de registrar su usuario y contraseña.".getBytes());
        } else {
            //String idS = request.getParameter("id");
            //int id2 = Integer.parseInt(idS);
            int id2 = id;
            MPlantilla plant = daoPlantillas.BuscarPlantilla(id2);
            String nombreplan = "";
            if (plant.getNombre() != null && !"".equals(plant.getNombre().trim())) {
                nombreplan = nombreplan + plant.getNombre().trim();
            }
            nombreplan = nombreplan + "_" + plant.getVersion();
            nombreplan = nombreplan + ".zip";
            if (plant != null) {
                response.setContentLength((int) plant.getPlantilla().length);
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=" + nombreplan);
                response.getOutputStream().write(plant.getPlantilla());
            }
        }
    }

    /**
     * @return the daoPlantillas
     */
    public PlantillaDAO getDaoPlantillas() {
        return daoPlantillas;
    }

    /**
     * @param daoPlantillas the daoPlantillas to set
     */
    public void setDaoPlantillas(PlantillaDAO daoPlantillas) {
        this.daoPlantillas = daoPlantillas;
    }
}
