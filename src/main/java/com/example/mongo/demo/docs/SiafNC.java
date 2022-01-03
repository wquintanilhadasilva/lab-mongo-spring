package com.example.mongo.demo.docs;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiafNC {

    @Id
    CompositeKey id;

    Long numero;
    LocalDate dataEmissao;
    String ugEmitente;
    String ugGestaoFavorecida;
    String observacao;
    String evento;
    String esfera;
    String ptres;
    String fonte;
    String ugr;
    String pi;
    Double valor;

    public void buildId(){
        this.id = new CompositeKey(this.numero, this.dataEmissao);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompositeKey implements Serializable {
        private Long numero;
        LocalDate dataEmissao;

        @Override
        public String toString() {
            return String.format(
                    "[numero=%s:dataEmissao='%s']",
                    numero, dataEmissao);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "SiafNC[id=%s, dataEmissao='%s', ugEmitente='%s']",
                id, dataEmissao, ugEmitente);
    }

}
