import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static class CaesarCipherApp {
        private final JFrame frame;
        private final JTextField filePathField;
        private final JTextField outputFilePathField;

        public CaesarCipherApp() {
            frame = new JFrame("Caesar Cipher");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 250);
            frame.setLayout(new FlowLayout());

            filePathField = new JTextField(20);
            outputFilePathField = new JTextField(20);
            JButton selectFileButton = new JButton("Выберите файл");
            JButton encryptButton = new JButton("Зашифровать");
            JButton decryptButton = new JButton("Расшифровать");

            selectFileButton.addActionListener(new SelectFileAction());
            encryptButton.addActionListener(new EncryptAction());
            decryptButton.addActionListener(new DecryptAction());

            frame.add(new JLabel("Исходный файл:"));
            frame.add(filePathField);
            frame.add(selectFileButton);
            frame.add(new JLabel("Выходной файл:"));
            frame.add(outputFilePathField);
            frame.add(encryptButton);
            frame.add(decryptButton);

            frame.setVisible(true);
        }

        // Действие для выбора файла
        private class SelectFileAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getPath());
                }
            }
        }

        // Действие для шифрования файла
        private class EncryptAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shiftInput = JOptionPane.showInputDialog(frame, "Введите сдвиг (0-50):");
                if (shiftInput == null || shiftInput.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, введите действительное число от 0 до 50.");
                    return;
                }
                try {
                    int shift = Integer.parseInt(shiftInput);
                    if (shift < 0 || shift > 50) {
                        throw new NumberFormatException("Сдвиг вне допустимого диапазона.");
                    }
                    processFile(true, shift);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, введите действительное число от 0 до 50.");
                }
            }
        }

        // Действие для расшифровки файла
        private class DecryptAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shiftInput = JOptionPane.showInputDialog(frame, "Введите сдвиг (0-50):");
                if (shiftInput == null || shiftInput.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, введите действительное число от 0 до 50.");
                    return;
                }
                try {
                    int shift = Integer.parseInt(shiftInput);
                    if (shift < 0 || shift > 50) {
                        throw new NumberFormatException("Сдвиг вне допустимого диапазона.");
                    }
                    processFile(false, shift);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, введите действительное число от 0 до 50.");
                }
            }
        }

        // Метод для обработки файла: шифрование или расшифровка
        private void processFile(boolean isEncrypt, int shift) {
            String inputPath = filePathField.getText();
            String outputPath = outputFilePathField.getText();

            if (inputPath.isEmpty() || outputPath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Пожалуйста, укажите пути к обоим файлам.");
                return;
            }

            Path inputFilePath = Path.of(inputPath);
            Path outputFilePath = Path.of(outputPath);

            try {
                if (!Files.exists(inputFilePath)) {
                    JOptionPane.showMessageDialog(frame, "Исходный файл не существует.");
                    return;
                }

                String content = Files.readString(inputFilePath);
                String processed = caesarCipher(content, isEncrypt ? shift : -shift);
                Files.writeString(outputFilePath, processed);
                JOptionPane.showMessageDialog(frame, "Успех! Результат сохранен в файл: " + outputFilePath);
            } catch (IOException e) {
                System.err.println("Ошибка: " + e.getMessage());
                JOptionPane.showMessageDialog(frame, "Произошла ошибка при обработке файла.");
            }
        }

        // Метод для выполнения шифра Цезаря
        private String caesarCipher(String text, int shift) {
            StringBuilder result = new StringBuilder();

            for (char ch : text.toCharArray()) {
                if (Character.isLetter(ch)) {
                    char base = Character.isLowerCase(ch) ? 'a' : 'A';
                    ch = (char) ((ch - base + shift + 26) % 26 + base);  // Сдвиг по алфавиту
                }
                result.append(ch);  // Добавляем символ, независимо от его типа
            }

            return result.toString();
        }

        // Запуск приложения
        public static void main(String[] args) {
            SwingUtilities.invokeLater(CaesarCipherApp::new);
        }
    }
}
