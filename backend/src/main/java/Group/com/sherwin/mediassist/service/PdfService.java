package Group.com.sherwin.mediassist.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    public String extractText(String filePath) throws IOException {

        PDDocument document = Loader.loadPDF(new File(filePath));

        PDFTextStripper stripper = new PDFTextStripper();

        String text = stripper.getText(document);

        document.close();

        return text;
    }
}