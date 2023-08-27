--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.2
-- Dumped by pg_dump version 9.5.2

-- Started on 2023-02-03 00:40:06
--DEBTESTE
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2346 (class 1262 OID 16393)
-- Name: DEBTESTE; Type: DATABASE; Schema: -; Owner: postgres
--
-- Sempre verificar o nome do banco de dados antes de rodar.
--CREATE DATABASE "DEBTESTE" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';


ALTER DATABASE "DEBTESTE" OWNER TO postgres;

--\connect "DEBTESTE"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


    --
-- TOC entry 2348 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

    COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 221 (class 1255 OID 17944)
-- Name: validachavepessoa(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validachavepessoa() RETURNS trigger
                                            LANGUAGE plpgsql
    AS $$
declare existe integer;
begin
            existe = (select count(1) from pessoa_fisica where id = NEW.pessoa_id);
    if(existe <=0 ) then
        existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_id);
    if (existe <= 0) then
        raise exception 'Não foi encontrado o ID ou PK da pessoa para realizar a associação';
end if;
end if;
RETURN NEW;
end;
$$;


ALTER FUNCTION public.validachavepessoa() OWNER TO postgres;

--
-- TOC entry 222 (class 1255 OID 17945)
-- Name: validachavepessoaforn(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validachavepessoaforn() RETURNS trigger
                                                LANGUAGE plpgsql
    AS $$

declare existe integer;

begin
            existe = (select count(1) from pessoa_fisica where id = NEW.pessoa_forn_id);
    if(existe <=0 ) then
        existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_forn_id);
    if (existe <= 0) then
        raise exception 'Não foi encontrado o ID ou PK da pessoa para realizar a associação';
end if;
end if;
RETURN NEW;
end;
$$;


ALTER FUNCTION public.validachavepessoaforn() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 181 (class 1259 OID 17685)
-- Name: acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE acesso (
                        id bigint NOT NULL,
                        descricao_acesso character varying(255) NOT NULL
);


ALTER TABLE acesso OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 17690)
-- Name: avaliacao_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE avaliacao_produto (
                                   id bigint NOT NULL,
                                   descricao_avaliacao_produto character varying(255) NOT NULL,
                                   nota_avaliacao_produto integer NOT NULL,
                                   pessoa_id bigint NOT NULL,
                                   produto_id bigint NOT NULL
);


ALTER TABLE avaliacao_produto OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 17695)
-- Name: categoria_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE categoria_produto (
                                   id bigint NOT NULL,
                                   descricao_categoria_produto character varying(255) NOT NULL
);


ALTER TABLE categoria_produto OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 17700)
-- Name: conta_pagar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conta_pagar (
                             id bigint NOT NULL,
                             descricao_conta_receber character varying(255) NOT NULL,
                             data_pagamento_conta_receber date,
                             data_vencimento_conta_receber date NOT NULL,
                             status_conta_pagar character varying(255) NOT NULL,
                             valor_desconto_conta_receber numeric(19,2) NOT NULL,
                             valor_total_conta_receber numeric(19,2) NOT NULL,
                             pessoa_id bigint NOT NULL,
                             pessoa_forn_id bigint NOT NULL
);


ALTER TABLE conta_pagar OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 17708)
-- Name: conta_receber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conta_receber (
                               id bigint NOT NULL,
                               descricao_conta_receber character varying(255) NOT NULL,
                               data_pagamento_conta_receber date,
                               data_vencimento_conta_receber date NOT NULL,
                               status_conta_receber character varying(255) NOT NULL,
                               valor_desconto_conta_receber numeric(19,2) NOT NULL,
                               valor_total_conta_receber numeric(19,2) NOT NULL,
                               pessoa_id bigint NOT NULL
);


ALTER TABLE conta_receber OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 17716)
-- Name: cup_desc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cup_desc (
                          id bigint NOT NULL,
                          codigo_desconto character varying(255) NOT NULL,
                          data_validade_cupom date NOT NULL,
                          valor_percentual_desconto numeric(19,2),
                          valor_real_desconto numeric(19,2)
);


ALTER TABLE cup_desc OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 17721)
-- Name: endereco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE endereco (
                          id bigint NOT NULL,
                          bairro character varying(255) NOT NULL,
                          cep character varying(255) NOT NULL,
                          cidade character varying(255) NOT NULL,
                          complemento character varying(255) NOT NULL,
                          estado character varying(255) NOT NULL,
                          logradouro character varying(255) NOT NULL,
                          numero character varying(255) NOT NULL,
                          tipo_endereco character varying(255) NOT NULL,
                          uf character varying(255) NOT NULL,
                          pessoa_id bigint NOT NULL
);


ALTER TABLE endereco OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 17729)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE forma_pagamento (
                                 id bigint NOT NULL,
                                 descricao_forma_pagamento character varying(255) NOT NULL
);


ALTER TABLE forma_pagamento OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 17734)
-- Name: imagem_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE imagem_produto (
                                id bigint NOT NULL,
                                imagem_miniatura text NOT NULL,
                                imagem_original text NOT NULL,
                                produto_id bigint NOT NULL
);


ALTER TABLE imagem_produto OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 17742)
-- Name: item_venda_loja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE item_venda_loja (
                                 id bigint NOT NULL,
                                 quantidade_item_venda_loja double precision NOT NULL,
                                 produto_id bigint NOT NULL,
                                 vd_cp_loja_virt_id bigint NOT NULL
);


ALTER TABLE item_venda_loja OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 17747)
-- Name: marca_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE marca_produto (
                               id bigint NOT NULL,
                               descricao_marca_produto character varying(255) NOT NULL
);


ALTER TABLE marca_produto OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 17752)
-- Name: nota_fiscal_compra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_fiscal_compra (
                                    id bigint NOT NULL,
                                    data_compra date NOT NULL,
                                    descricao_observacao_nota character varying(255) NOT NULL,
                                    numero_nota_fiscal character varying(255) NOT NULL,
                                    serie_nota_fiscal character varying(255) NOT NULL,
                                    valor_desconto_nota_compra numeric(19,2) NOT NULL,
                                    valor_icms_nota_compra numeric(19,2) NOT NULL,
                                    valor_total_nota_compra numeric(19,2) NOT NULL,
                                    conta_pagar_id bigint NOT NULL,
                                    pessoa_id bigint NOT NULL
);


ALTER TABLE nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 17760)
-- Name: nota_fiscal_venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_fiscal_venda (
                                   id bigint NOT NULL,
                                   numero_nota_fiscal_venda character varying(255) NOT NULL,
                                   pdf_nota_fiscal_venda text NOT NULL,
                                   serie_nota_fiscal_venda character varying(255) NOT NULL,
                                   tipo_nota_fiscal_venda character varying(255) NOT NULL,
                                   xml_nota_fiscal_venda text NOT NULL,
                                   vd_cp_loja_virt_id bigint NOT NULL
);


ALTER TABLE nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 17768)
-- Name: nota_item_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_item_produto (
                                   id bigint NOT NULL,
                                   quantidade_produto double precision NOT NULL,
                                   nota_fiscal_compra_id bigint NOT NULL,
                                   produto_id bigint NOT NULL
);


ALTER TABLE nota_item_produto OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 17773)
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pessoa_fisica (
                               id bigint NOT NULL,
                               email character varying(255) NOT NULL,
                               nome_pessoa character varying(255) NOT NULL,
                               telefone character varying(255) NOT NULL,
                               cpf character varying(255) NOT NULL,
                               data_nascimento date NOT NULL
);


ALTER TABLE pessoa_fisica OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 17781)
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pessoa_juridica (
                                 id bigint NOT NULL,
                                 email character varying(255) NOT NULL,
                                 nome_pessoa character varying(255) NOT NULL,
                                 telefone character varying(255) NOT NULL,
                                 categoria character varying(255) NOT NULL,
                                 cnpj character varying(255) NOT NULL,
                                 inscricao_estadual character varying(255) NOT NULL,
                                 inscricao_municipal character varying(255) NOT NULL,
                                 nome_fantasia character varying(255) NOT NULL,
                                 razao_social character varying(255) NOT NULL
);


ALTER TABLE pessoa_juridica OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 17789)
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE produto (
                         id bigint NOT NULL,
                         alerta_quantidade_estoque boolean NOT NULL,
                         altura_produto double precision NOT NULL,
                         descricao_produto text NOT NULL,
                         largura_produto double precision NOT NULL,
                         link_youtube_produto character varying(255),
                         nome_produto character varying(255) NOT NULL,
                         peso_produto double precision NOT NULL,
                         profundidade_produto double precision NOT NULL,
                         quantidade_alerta_estoque_produto integer NOT NULL,
                         quantidade_clique_produto integer,
                         quantidade_estoque_produto integer NOT NULL,
                         tipo_unidade character varying(255) NOT NULL,
                         valor_total_venda numeric(19,2) NOT NULL
);


ALTER TABLE produto OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 17825)
-- Name: seq_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_acesso OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 17827)
-- Name: seq_avaliacao_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_avaliacao_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_avaliacao_produto OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 17829)
-- Name: seq_categoria_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_categoria_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_categoria_produto OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 17831)
-- Name: seq_conta_pagar; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_conta_pagar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_conta_pagar OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 17833)
-- Name: seq_conta_receber; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_conta_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_conta_receber OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 17835)
-- Name: seq_cup_desc; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_cup_desc
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_cup_desc OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 17837)
-- Name: seq_endereco; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_endereco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_endereco OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 17839)
-- Name: seq_forma_pagamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_forma_pagamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_forma_pagamento OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 17841)
-- Name: seq_imagem_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_imagem_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_imagem_produto OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 17843)
-- Name: seq_item_venda_loja; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_item_venda_loja
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_item_venda_loja OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 17845)
-- Name: seq_marca_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_marca_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_marca_produto OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 17847)
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_fiscal_compra
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 17849)
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_fiscal_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 17851)
-- Name: seq_nota_item_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_item_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_item_produto OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17853)
-- Name: seq_pessoa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_pessoa OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17855)
-- Name: seq_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_produto OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17857)
-- Name: seq_status_rastreio; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_status_rastreio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_status_rastreio OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17859)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_usuario OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17861)
-- Name: seq_vd_cp_loja_virt; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_vd_cp_loja_virt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_vd_cp_loja_virt OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 17797)
-- Name: status_rastreio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE status_rastreio (
                                 id bigint NOT NULL,
                                 centro_distribuicao character varying(255) NOT NULL,
                                 cidade character varying(255) NOT NULL,
                                 estado character varying(255) NOT NULL,
                                 status_rastreio character varying(255) NOT NULL,
                                 vd_cp_loja_virt_id bigint NOT NULL
);


ALTER TABLE status_rastreio OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 17805)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuario (
                         id bigint NOT NULL,
                         data_atual_senha date NOT NULL,
                         login character varying(255) NOT NULL,
                         senha character varying(255) NOT NULL,
                         pessoa_id bigint NOT NULL
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 17813)
-- Name: usuarios_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuarios_acesso (
                                 usuario_id bigint NOT NULL,
                                 acesso_id bigint NOT NULL
);


ALTER TABLE usuarios_acesso OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 17816)
-- Name: vd_cp_loja_virt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE vd_cp_loja_virt (
                                 id bigint NOT NULL,
                                 dias_entrega integer NOT NULL,
                                 data_entrega date NOT NULL,
                                 data_venda date NOT NULL,
                                 valor_total_desconto_venda_loja numeric(19,2) NOT NULL,
                                 valor_total_frete numeric(19,2) NOT NULL,
                                 valor_total_venda_loja numeric(19,2) NOT NULL,
                                 cupom_desconto_id bigint NOT NULL,
                                 endereco_cobranca_id bigint NOT NULL,
                                 endereco_entrega_id bigint NOT NULL,
                                 forma_pagamento_id bigint NOT NULL,
                                 nota_fiscal_venda_id bigint NOT NULL,
                                 pessoa_id bigint NOT NULL
);


ALTER TABLE vd_cp_loja_virt OWNER TO postgres;

--
-- TOC entry 2302 (class 0 OID 17685)
-- Dependencies: 181
-- Data for Name: acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2303 (class 0 OID 17690)
-- Dependencies: 182
-- Data for Name: avaliacao_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2304 (class 0 OID 17695)
-- Dependencies: 183
-- Data for Name: categoria_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2305 (class 0 OID 17700)
-- Dependencies: 184
-- Data for Name: conta_pagar; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2306 (class 0 OID 17708)
-- Dependencies: 185
-- Data for Name: conta_receber; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2307 (class 0 OID 17716)
-- Dependencies: 186
-- Data for Name: cup_desc; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2308 (class 0 OID 17721)
-- Dependencies: 187
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2309 (class 0 OID 17729)
-- Dependencies: 188
-- Data for Name: forma_pagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2310 (class 0 OID 17734)
-- Dependencies: 189
-- Data for Name: imagem_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2311 (class 0 OID 17742)
-- Dependencies: 190
-- Data for Name: item_venda_loja; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2312 (class 0 OID 17747)
-- Dependencies: 191
-- Data for Name: marca_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2313 (class 0 OID 17752)
-- Dependencies: 192
-- Data for Name: nota_fiscal_compra; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2314 (class 0 OID 17760)
-- Dependencies: 193
-- Data for Name: nota_fiscal_venda; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2315 (class 0 OID 17768)
-- Dependencies: 194
-- Data for Name: nota_item_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2316 (class 0 OID 17773)
-- Dependencies: 195
-- Data for Name: pessoa_fisica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2317 (class 0 OID 17781)
-- Dependencies: 196
-- Data for Name: pessoa_juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2318 (class 0 OID 17789)
-- Dependencies: 197
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2349 (class 0 OID 0)
-- Dependencies: 202
-- Name: seq_acesso; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_acesso', 1, false);


--
-- TOC entry 2350 (class 0 OID 0)
-- Dependencies: 203
-- Name: seq_avaliacao_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_avaliacao_produto', 1, false);


--
-- TOC entry 2351 (class 0 OID 0)
-- Dependencies: 204
-- Name: seq_categoria_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_categoria_produto', 1, false);


--
-- TOC entry 2352 (class 0 OID 0)
-- Dependencies: 205
-- Name: seq_conta_pagar; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_conta_pagar', 1, false);


--
-- TOC entry 2353 (class 0 OID 0)
-- Dependencies: 206
-- Name: seq_conta_receber; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_conta_receber', 1, false);


--
-- TOC entry 2354 (class 0 OID 0)
-- Dependencies: 207
-- Name: seq_cup_desc; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_cup_desc', 1, false);


--
-- TOC entry 2355 (class 0 OID 0)
-- Dependencies: 208
-- Name: seq_endereco; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_endereco', 1, false);


--
-- TOC entry 2356 (class 0 OID 0)
-- Dependencies: 209
-- Name: seq_forma_pagamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_forma_pagamento', 1, false);


--
-- TOC entry 2357 (class 0 OID 0)
-- Dependencies: 210
-- Name: seq_imagem_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_imagem_produto', 1, false);


--
-- TOC entry 2358 (class 0 OID 0)
-- Dependencies: 211
-- Name: seq_item_venda_loja; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_item_venda_loja', 1, false);


--
-- TOC entry 2359 (class 0 OID 0)
-- Dependencies: 212
-- Name: seq_marca_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_marca_produto', 1, false);


--
-- TOC entry 2360 (class 0 OID 0)
-- Dependencies: 213
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_fiscal_compra', 1, false);


--
-- TOC entry 2361 (class 0 OID 0)
-- Dependencies: 214
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_fiscal_venda', 1, false);


--
-- TOC entry 2362 (class 0 OID 0)
-- Dependencies: 215
-- Name: seq_nota_item_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_item_produto', 1, false);


--
-- TOC entry 2363 (class 0 OID 0)
-- Dependencies: 216
-- Name: seq_pessoa; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_pessoa', 1, false);


--
-- TOC entry 2364 (class 0 OID 0)
-- Dependencies: 217
-- Name: seq_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_produto', 1, false);


--
-- TOC entry 2365 (class 0 OID 0)
-- Dependencies: 218
-- Name: seq_status_rastreio; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_status_rastreio', 1, false);


--
-- TOC entry 2366 (class 0 OID 0)
-- Dependencies: 219
-- Name: seq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_usuario', 1, false);


--
-- TOC entry 2367 (class 0 OID 0)
-- Dependencies: 220
-- Name: seq_vd_cp_loja_virt; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_vd_cp_loja_virt', 1, false);


--
-- TOC entry 2319 (class 0 OID 17797)
-- Dependencies: 198
-- Data for Name: status_rastreio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2320 (class 0 OID 17805)
-- Dependencies: 199
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2321 (class 0 OID 17813)
-- Dependencies: 200
-- Data for Name: usuarios_acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2322 (class 0 OID 17816)
-- Dependencies: 201
-- Data for Name: vd_cp_loja_virt; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2111 (class 2606 OID 17689)
-- Name: acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acesso
ADD CONSTRAINT acesso_pkey PRIMARY KEY (id);


--
-- TOC entry 2113 (class 2606 OID 17694)
-- Name: avaliacao_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
ADD CONSTRAINT avaliacao_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2115 (class 2606 OID 17699)
-- Name: categoria_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categoria_produto
ADD CONSTRAINT categoria_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2117 (class 2606 OID 17707)
-- Name: conta_pagar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
ADD CONSTRAINT conta_pagar_pkey PRIMARY KEY (id);


--
-- TOC entry 2119 (class 2606 OID 17715)
-- Name: conta_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_receber
ADD CONSTRAINT conta_receber_pkey PRIMARY KEY (id);


--
-- TOC entry 2121 (class 2606 OID 17720)
-- Name: cup_desc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cup_desc
ADD CONSTRAINT cup_desc_pkey PRIMARY KEY (id);


--
-- TOC entry 2123 (class 2606 OID 17728)
-- Name: endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 2125 (class 2606 OID 17733)
-- Name: forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY forma_pagamento
ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2127 (class 2606 OID 17741)
-- Name: imagem_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
ADD CONSTRAINT imagem_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2129 (class 2606 OID 17746)
-- Name: item_venda_loja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
ADD CONSTRAINT item_venda_loja_pkey PRIMARY KEY (id);


--
-- TOC entry 2131 (class 2606 OID 17751)
-- Name: marca_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY marca_produto
ADD CONSTRAINT marca_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2133 (class 2606 OID 17759)
-- Name: nota_fiscal_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
ADD CONSTRAINT nota_fiscal_compra_pkey PRIMARY KEY (id);


--
-- TOC entry 2135 (class 2606 OID 17767)
-- Name: nota_fiscal_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
ADD CONSTRAINT nota_fiscal_venda_pkey PRIMARY KEY (id);


--
-- TOC entry 2137 (class 2606 OID 17772)
-- Name: nota_item_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
ADD CONSTRAINT nota_item_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2139 (class 2606 OID 17780)
-- Name: pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_fisica
ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id);


--
-- TOC entry 2141 (class 2606 OID 17788)
-- Name: pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_juridica
ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id);


--
-- TOC entry 2143 (class 2606 OID 17796)
-- Name: produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
ADD CONSTRAINT produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2145 (class 2606 OID 17804)
-- Name: status_rastreio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
ADD CONSTRAINT status_rastreio_pkey PRIMARY KEY (id);


--
-- TOC entry 2149 (class 2606 OID 17822)
-- Name: uk_8bak9jswon2id2jbunuqlfl9e; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
ADD CONSTRAINT uk_8bak9jswon2id2jbunuqlfl9e UNIQUE (acesso_id);


--
-- TOC entry 2151 (class 2606 OID 17824)
-- Name: unique_acesso_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
ADD CONSTRAINT unique_acesso_user UNIQUE (usuario_id, acesso_id);


--
-- TOC entry 2147 (class 2606 OID 17812)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2153 (class 2606 OID 17820)
-- Name: vd_cp_loja_virt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
ADD CONSTRAINT vd_cp_loja_virt_pkey PRIMARY KEY (id);


--
-- TOC entry 2171 (class 2620 OID 17955)
-- Name: validachavepessoaavaliacaoprodutoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutoinsert BEFORE INSERT ON avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2170 (class 2620 OID 17954)
-- Name: validachavepessoaavaliacaoprodutoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutoupdate BEFORE UPDATE ON avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2173 (class 2620 OID 17957)
-- Name: validachavepessoacontapagarinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagarinsert BEFORE INSERT ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2172 (class 2620 OID 17956)
-- Name: validachavepessoacontapagarupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagarupdate BEFORE UPDATE ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2177 (class 2620 OID 17961)
-- Name: validachavepessoacontareceberinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontareceberinsert BEFORE INSERT ON conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2176 (class 2620 OID 17960)
-- Name: validachavepessoacontareceberupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontareceberupdate BEFORE UPDATE ON conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2181 (class 2620 OID 17967)
-- Name: validachavepessoaenderecoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecoinsert BEFORE INSERT ON endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2180 (class 2620 OID 17966)
-- Name: validachavepessoaenderecoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecoupdate BEFORE UPDATE ON endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2175 (class 2620 OID 17965)
-- Name: validachavepessoaforncontapagarinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaforncontapagarinsert BEFORE INSERT ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoaforn();


--
-- TOC entry 2174 (class 2620 OID 17964)
-- Name: validachavepessoaforncontapagarupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaforncontapagarupdate BEFORE UPDATE ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoaforn();


--
-- TOC entry 2179 (class 2620 OID 17963)
-- Name: validachavepessoaforncontareceberinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaforncontareceberinsert BEFORE INSERT ON conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoaforn();


--
-- TOC entry 2178 (class 2620 OID 17962)
-- Name: validachavepessoaforncontareceberupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaforncontareceberupdate BEFORE UPDATE ON conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoaforn();


--
-- TOC entry 2183 (class 2620 OID 17969)
-- Name: validachavepessoanotafiscalcomprainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotafiscalcomprainsert BEFORE INSERT ON nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2182 (class 2620 OID 17968)
-- Name: validachavepessoanotafiscalcompraupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotafiscalcompraupdate BEFORE UPDATE ON nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2185 (class 2620 OID 17971)
-- Name: validachavepessoausuarioinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuarioinsert BEFORE INSERT ON usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2184 (class 2620 OID 17970)
-- Name: validachavepessoausuarioupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuarioupdate BEFORE UPDATE ON usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2187 (class 2620 OID 17973)
-- Name: validachavepessoavdcplojavirtinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoavdcplojavirtinsert BEFORE INSERT ON vd_cp_loja_virt FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2186 (class 2620 OID 17972)
-- Name: validachavepessoavdcplojavirtupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoavdcplojavirtupdate BEFORE UPDATE ON vd_cp_loja_virt FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2163 (class 2606 OID 17908)
-- Name: acesso_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
ADD CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES acesso(id);


--
-- TOC entry 2158 (class 2606 OID 17883)
-- Name: conta_pagar_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
ADD CONSTRAINT conta_pagar_fk FOREIGN KEY (conta_pagar_id) REFERENCES conta_pagar(id);


--
-- TOC entry 2165 (class 2606 OID 17918)
-- Name: cupom_desconto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
ADD CONSTRAINT cupom_desconto_fk FOREIGN KEY (cupom_desconto_id) REFERENCES cup_desc(id);


--
-- TOC entry 2166 (class 2606 OID 17923)
-- Name: endereco_cobranca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
ADD CONSTRAINT endereco_cobranca_fk FOREIGN KEY (endereco_cobranca_id) REFERENCES endereco(id);


--
-- TOC entry 2167 (class 2606 OID 17928)
-- Name: endereco_entrega_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
ADD CONSTRAINT endereco_entrega_fk FOREIGN KEY (endereco_entrega_id) REFERENCES endereco(id);


--
-- TOC entry 2168 (class 2606 OID 17933)
-- Name: forma_pagamento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
ADD CONSTRAINT forma_pagamento_fk FOREIGN KEY (forma_pagamento_id) REFERENCES forma_pagamento(id);


--
-- TOC entry 2160 (class 2606 OID 17893)
-- Name: nota_fiscal_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
ADD CONSTRAINT nota_fiscal_compra_fk FOREIGN KEY (nota_fiscal_compra_id) REFERENCES nota_fiscal_compra(id);


--
-- TOC entry 2169 (class 2606 OID 17938)
-- Name: nota_fiscal_venda_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
ADD CONSTRAINT nota_fiscal_venda_fk FOREIGN KEY (nota_fiscal_venda_id) REFERENCES nota_fiscal_venda(id);


--
-- TOC entry 2154 (class 2606 OID 17863)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2155 (class 2606 OID 17868)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2156 (class 2606 OID 17873)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2161 (class 2606 OID 17898)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2164 (class 2606 OID 17913)
-- Name: usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
ADD CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES usuario(id);


--
-- TOC entry 2157 (class 2606 OID 17878)
-- Name: vd_cp_loja_virt_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
ADD CONSTRAINT vd_cp_loja_virt_fk FOREIGN KEY (vd_cp_loja_virt_id) REFERENCES vd_cp_loja_virt(id);


--
-- TOC entry 2159 (class 2606 OID 17888)
-- Name: vd_cp_loja_virt_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
ADD CONSTRAINT vd_cp_loja_virt_fk FOREIGN KEY (vd_cp_loja_virt_id) REFERENCES vd_cp_loja_virt(id);


--
-- TOC entry 2162 (class 2606 OID 17903)
-- Name: vd_cp_loja_virt_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
ADD CONSTRAINT vd_cp_loja_virt_fk FOREIGN KEY (vd_cp_loja_virt_id) REFERENCES vd_cp_loja_virt(id);


--
-- TOC entry 2347 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;


-- Completed on 2023-02-03 00:40:06

--
-- PostgreSQL database dump complete
--