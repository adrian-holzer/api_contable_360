package com.adri.api_contable_360.services;

import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import com.adri.api_contable_360.repositories.ObligacionRepository;
import com.adri.api_contable_360.repositories.VencimientoRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObligacionService {

    @Autowired
    private ObligacionRepository obligacionRepository;

    @Autowired
    private VencimientoRepository vencimientoRepository;

    public void procesarExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Obligacion> obligacionesMap = new HashMap<>();
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return;
            }

            // Leer el año desde la primera celda (fila 0, columna 0)
            int anio = 0;
            Cell anioCell = headerRow.getCell(0); // Obtener la primera celda
            if (anioCell != null) {
                if (anioCell.getCellType() == CellType.NUMERIC) {
                    anio = (int) anioCell.getNumericCellValue();
                } else if (anioCell.getCellType() == CellType.STRING) {
                    try {
                        anio = Integer.parseInt(anioCell.getStringCellValue().trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Error: No se pudo convertir el año desde la celda: " + anioCell.getStringCellValue());
                        // Aquí podrías decidir si quieres lanzar una excepción o usar un valor por defecto
                        anio = 0; // o lanzar una excepción
                    }
                }
            }
            if(anio == 0){
                System.err.println("Error: No se pudo obtener el año desde la primera celda. Se usará el año actual.");
                anio = Year.now().getValue();
            }

            System.out.println("Año leído desde el Excel: " + anio);

            List<String> obligacionHeaders = new ArrayList<>();
            List<Integer> obligacionColumnStarts = new ArrayList<>();

            for (int i = 1; i < headerRow.getLastCellNum(); ) {
                Cell headerCell = headerRow.getCell(i);
                if (headerCell != null && headerCell.getCellType() == CellType.STRING) {
                    String obligacionNombre = headerCell.getStringCellValue().trim();
                    if (!obligacionNombre.isEmpty() && !obligacionHeaders.contains(obligacionNombre)) {
                        obligacionHeaders.add(obligacionNombre);
                        obligacionColumnStarts.add(i);
                        i += 10;
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row dataRow = sheet.getRow(rowIndex);
                if (dataRow == null) continue;

                String mesTexto = getCellValueAsString(dataRow.getCell(0));
                int mes = obtenerNumeroMes(mesTexto);
                System.out.println("Procesando fila: " + (rowIndex + 1) + ", Mes texto: " + mesTexto + ", Mes número: " + mes); // Log

                if (mes== 9 ){

                    System.out.println("Encontrado mes de SEPTIEMBRE - Depurando columnas..."); // Log

                }


                if (mes == 0) continue;

                for (int i = 0; i < obligacionHeaders.size(); i++) {
                    String obligacionNombre = obligacionHeaders.get(i);
                    int columnaInicio = obligacionColumnStarts.get(i);
                    Obligacion obligacion = obligacionesMap.computeIfAbsent(obligacionNombre, nombre -> {
                        Obligacion o = new Obligacion();
                        o.setNombre(nombre);
                        o.setActivo(true);
                        return obligacionRepository.save(o);
                    });

                    for (int j = 0; j < 10; j++) {
                        int terminacionColumna = columnaInicio + j;
                        if (terminacionColumna < dataRow.getLastCellNum()) {
                            Cell terminacionCell = dataRow.getCell(terminacionColumna);
                            if (terminacionCell != null && terminacionCell.getCellType() == CellType.NUMERIC) {
                                int terminacionDia = (int) terminacionCell.getNumericCellValue();
                                Vencimiento vencimiento = new Vencimiento();
                                vencimiento.setObligacion(obligacion);
                                vencimiento.setTerminacionCuit(j);
                                vencimiento.setMes(mes);
                                vencimiento.setAnio(anio);
                                vencimiento.setDia(terminacionDia);

                                try {
                                    vencimiento.setFechaVencimiento(LocalDate.of(anio, vencimiento.getMes(), vencimiento.getDia()));
                                } catch (Exception e) {
                                    // Manejar casos de fechas inválidas (ej: día 31 en mes de 30)
                                    vencimiento.setFechaVencimiento(null); // O podrías usar otra estrategia
                                    System.err.println("Fecha de vencimiento inválida para obligación " + obligacion.getId() + ": Mes=" + vencimiento.getMes() + ", Día=" + vencimiento.getDia());
                                }

                                vencimientoRepository.save(vencimiento);
                                System.out.println("    Guardado vencimiento: Mes=" + mes + ", Terminación=" + j + ", Día=" + terminacionDia); // Log

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private int obtenerNumeroMes(String mesTexto) {
        switch (mesTexto.toUpperCase()) {
            case "ENERO": return 1;
            case "FEBRERO": return 2;
            case "MARZO": return 3;
            case "ABRIL": return 4;
            case "MAYO": return 5;
            case "JUNIO": return 6;
            case "JULIO": return 7;
            case "AGOSTO": return 8;
            case "SETIEMBRE": return 9;
            case "OCTUBRE": return 10;
            case "NOVIEMBRE": return 11;
            case "DICIEMBRE": return 12;
            default: return 0;
        }
    }


    public List<Obligacion> listarTodasLasObligaciones() {
        return obligacionRepository.findAll();
    }


    public Obligacion findById(Long id) {
        return obligacionRepository.findById(id).orElse(null);
    }


    public List<Vencimiento> listarVencimientosPorObligacion(Long idObligacion) {

        Obligacion o = obligacionRepository.findById(idObligacion).get();


        return vencimientoRepository.findByObligacion(o);
    }




    public Obligacion crearObligacion(Obligacion obligacion) {
        return obligacionRepository.save(obligacion);
    }



}