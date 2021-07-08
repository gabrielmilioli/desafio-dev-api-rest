create schema gestao_contas;

create table gestao_contas.pessoas (
	id bigserial not null primary key,
	nome character varying(200) not null,
	cpf character varying(11) not null,
	data_nascimento date not null
);

create table gestao_contas.contas (
	id bigserial not null primary key,
	i_pessoas bigserial not null references gestao_contas.pessoas(id),
	saldo numeric(10,2) not null,
	limite_saque_diario numeric(10,2) not null,
	ativo character varying(3),
	tipo_conta numeric(1),
	data_criacao date not null default CURRENT_DATE,
    constraint lancamento_ativo check (ativo::text = any (array['SIM'::character varying, 'NAO'::character varying]::text[]))
);

create table gestao_contas.transacoes (
	id bigserial not null primary key,
	i_contas bigserial not null references gestao_contas.contas(id),
	valor numeric(10,2) not null,
	data_transacao date not null default CURRENT_DATE
);

insert into gestao_contas.pessoas (nome, cpf, data_nascimento)
values ('Gabriel', '77465970091', '1996-12-13');
