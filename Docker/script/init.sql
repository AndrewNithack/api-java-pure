CREATE TABLE fabricante (
                                          idFabricante NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                          nomeFabricante VARCHAR2(50)
    );

CREATE TABLE usuario (
                                       lgn NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                       prenome VARCHAR2(20) NOT NULL,
    sobrenome VARCHAR2(50) NOT NULL,
    pwr VARCHAR2(300) NOT NULL
    );

CREATE TABLE peca (
                                    idPeca NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    nomePeca VARCHAR2(50) NOT NULL,
    preco NUMBER,
    idFabricante NUMBER,
    CONSTRAINT fk_fabricante FOREIGN KEY (idFabricante) REFERENCES fabricante(idFabricante)
    );
