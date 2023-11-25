package com.proyecto.ecommerce.util;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;
import com.proyecto.ecommerce.model.Orden;

@Component("usuario/compras")
public class ListarOrdenesPdf extends AbstractPdfView {

	@SuppressWarnings("null")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		    @SuppressWarnings("unchecked")
			List<Orden> listaOrdenes = (List<Orden>) model.get("ordenes");
		    
		    document.setPageSize(PageSize.LETTER.rotate());
		    document.open();
		    
		    PdfPTable titulo = new PdfPTable(1);
		    @SuppressWarnings("unused")
			Font fuenteTitulo = FontFactory.getFont("Helvetica", 24, Color.black);
		    PdfPCell celda = new PdfPCell(new Phrase("Lista ordenes del cliente", fuenteTitulo));
		    		    
		    celda.setBorder(0);
		    celda.setBackgroundColor(new Color(49, 232, 250));
		    celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		    celda.setPadding(10);
		    titulo.addCell(celda);
			titulo.setSpacingAfter(15);
		    
			PdfPTable cabeceraLista = new PdfPTable(4);
			Font fuenteCabecera = FontFactory.getFont("Helvetica", 14, Color.black);
			PdfPCell cabCell = new PdfPCell(new Phrase("Id",fuenteCabecera));
		    cabeceraLista.addCell("Id");
		    cabeceraLista.addCell("No. Compra");
		    cabeceraLista.addCell("Fecha");
		    cabeceraLista.addCell("Total");
			
			
			
			PdfPTable tablaOrdenes = new PdfPTable(4);
			tablaOrdenes.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			listaOrdenes.forEach(ordenes->{
				tablaOrdenes.addCell(ordenes.getId().toString());
				tablaOrdenes.addCell(ordenes.getNumero());
				tablaOrdenes.addCell(ordenes.getFechaCreacion().toString());
				tablaOrdenes.addCell(String.valueOf(ordenes.getTotal()));
			});
			
			
			document.add(titulo);
			document.add(cabeceraLista);
			document.add(tablaOrdenes);
		
	}

}
