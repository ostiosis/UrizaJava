# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table component (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  code                      varchar(255),
  user_id                   bigint,
  date_created              date,
  date_modified             date,
  constraint pk_component primary key (id))
;

create table page (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  title                     varchar(255),
  description               varchar(255),
  user_id                   bigint,
  date_created              date,
  date_modified             date,
  constraint pk_page primary key (id))
;

create table password_reset (
  user_id                   bigint,
  token                     varchar(255),
  expires                   datetime)
;

create table template (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  code                      varchar(255),
  user_id                   bigint,
  date_created              date,
  date_modified             date,
  constraint pk_template primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  email                     varchar(255),
  password_hash             varchar(255),
  join_date                 date,
  constraint pk_user primary key (id))
;


create table template_component (
  component_id                   bigint not null,
  template_id                    bigint not null,
  constraint pk_template_component primary key (component_id, template_id))
;

create table page_template (
  page_id                        bigint not null,
  template_id                    bigint not null,
  constraint pk_page_template primary key (page_id, template_id))
;
alter table component add constraint fk_component_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_component_user_1 on component (user_id);
alter table page add constraint fk_page_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_page_user_2 on page (user_id);
alter table password_reset add constraint fk_password_reset_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_password_reset_user_3 on password_reset (user_id);
alter table template add constraint fk_template_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_template_user_4 on template (user_id);



alter table template_component add constraint fk_template_component_component_01 foreign key (component_id) references component (id) on delete restrict on update restrict;

alter table template_component add constraint fk_template_component_template_02 foreign key (template_id) references template (id) on delete restrict on update restrict;

alter table page_template add constraint fk_page_template_page_01 foreign key (page_id) references page (id) on delete restrict on update restrict;

alter table page_template add constraint fk_page_template_template_02 foreign key (template_id) references template (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table component;

drop table template_component;

drop table page;

drop table page_template;

drop table password_reset;

drop table template;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

