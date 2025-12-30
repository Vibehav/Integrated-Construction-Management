create table user_roles
(
    user_id bigint not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references users,
    roles   varchar(255)
        constraint user_roles_roles_check
            check ((roles)::text = ANY
                   ((ARRAY ['ADMIN'::character varying, 'SUPER_MANAGER'::character varying, 'SITE_ENGINEER'::character varying, 'SAFETY_OFFICER'::character varying, 'ACCOUNTANT'::character varying, 'STORE_KEEPER'::character varying, 'CONTRACTOR'::character varying])::text[]))
);

alter table user_roles
    owner to vaibhav;

