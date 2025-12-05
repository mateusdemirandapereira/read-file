use conciliacao;



create table if not exists spc(
spc_codigo bigint,
 spc_cpf varchar(255),
 spc_cliente_nome varchar(255),
 spc_contrato_parcela varchar(255),
 spc_data_vencimento date,
 spc_valor_debito decimal(15,6),
 spc_data_inclusao date,
 spc_hora_inclusao time,
 spc_data_exclusao date,
 spc_tipo_notificacao varchar(255),
 spc_codigo_notificacao bigint,
 spc_codigo_associado int);
 
 

 create table if not exists serasa(
	serasa_status varchar(255) not null,
	serasa_codigo bigint not null,
    serasa_nome varchar(255) not null,
    serasa_tipo varchar(255) not null,
    serasa_cpf varchar(255) not null,
    serasa_natureza varchar(255) not null,
    serasa_valor decimal(15,6) not null,
    serasa_data_cadastro date not null,
    serasa_hora_cadastro time not null,
    serasa_data_vencimento date not null,
    serasa_operacao varchar(255) not null);
 
 

 
 create table if not exists protesto(
	pedido bigint not null,
    comarcaCartorio varchar(255) not null,
    dataSolicitacao date not null,
    comarcaDevedor varchar(255) not null,
    devedor varchar(255) not null,
    docDevedor bigint not null,
    numeroTitulo  varchar(255) not null,
    valorTitulo decimal(15,6) not null,
    valorProtesto decimal(15,6) not null,
    protocolo bigint not null,
    dataProtocolo date null,
    especie varchar(255) not null,
    statusPedido varchar(255) not null,
    irregularidade varchar(255),
    ocorrenciaTitulo varchar(255),
    dataOcorrencia date);