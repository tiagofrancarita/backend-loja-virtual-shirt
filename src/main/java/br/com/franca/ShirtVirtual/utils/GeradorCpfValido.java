package br.com.franca.ShirtVirtual.utils;

import org.springframework.stereotype.Component;

@Component
public class GeradorCpfValido {

    public String generateRandomCpf() {
        String cpfSemDigito = generateRandomDigits(9);
        String digito1 = calculateCpfDigit(cpfSemDigito);
        String digito2 = calculateCpfDigit(cpfSemDigito + digito1);
        return cpfSemDigito + digito1 + digito2;
    }

    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    private String calculateCpfDigit(String str) {
        int sum = 0;
        int weight = 2;

        for (int i = str.length() - 1; i >= 0; i--) {
            sum += Integer.parseInt(str.substring(i, i + 1)) * weight;
            weight = (weight == 9) ? 2 : weight + 1;
        }

        int remainder = sum % 11;
        return (remainder < 2) ? "0" : String.valueOf(11 - remainder);
    }

}