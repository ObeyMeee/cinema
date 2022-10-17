-- OWNER
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('bb20312a-f423-4497-92ea-2b8a25a702bd', (SELECT id FROM roles WHERE name = 'ROLE_OWNER'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('bb20312a-f423-4497-92ea-2b8a25a702bd', (SELECT id FROM roles WHERE name = 'ROLE_SUPER_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('bb20312a-f423-4497-92ea-2b8a25a702bd', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('bb20312a-f423-4497-92ea-2b8a25a702bd', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

-- SUPER_ADMINS
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('f6996e9b-5d67-4acd-9aad-ebf345884068', (SELECT id FROM roles WHERE name = 'ROLE_SUPER_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('f6996e9b-5d67-4acd-9aad-ebf345884068', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('f6996e9b-5d67-4acd-9aad-ebf345884068', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('4d9f478a-0dae-44f8-b462-bd8bc6afd580', (SELECT id FROM roles WHERE name = 'ROLE_SUPER_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('4d9f478a-0dae-44f8-b462-bd8bc6afd580', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('4d9f478a-0dae-44f8-b462-bd8bc6afd580', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('0b5df75d-111a-4455-b2cc-1e21034b0c46', (SELECT id FROM roles WHERE name = 'ROLE_SUPER_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('0b5df75d-111a-4455-b2cc-1e21034b0c46', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('0b5df75d-111a-4455-b2cc-1e21034b0c46', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('90c63ffb-2f9c-40f4-a208-6a9b1de939f2', (SELECT id FROM roles WHERE name = 'ROLE_SUPER_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('90c63ffb-2f9c-40f4-a208-6a9b1de939f2', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('90c63ffb-2f9c-40f4-a208-6a9b1de939f2', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('91c54ffb-3f8d-12f3-a234-6f3a2ff33711', (SELECT id FROM roles WHERE name = 'ROLE_SUPER_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('91c54ffb-3f8d-12f3-a234-6f3a2ff33711', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('91c54ffb-3f8d-12f3-a234-6f3a2ff33711', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

-- ADMINS
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('fb32b44a-9172-4616-aedf-a4c657e7cf15', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('fb32b44a-9172-4616-aedf-a4c657e7cf15', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('641d1f6a-ddc2-43bf-95b0-ebd09ae22be5', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('641d1f6a-ddc2-43bf-95b0-ebd09ae22be5', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('3dd9b32c-b2ae-41e4-807f-f2e36243648d', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('3dd9b32c-b2ae-41e4-807f-f2e36243648d', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('42afa94b-8a2b-47f4-8244-4852f140c820', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('42afa94b-8a2b-47f4-8244-4852f140c820', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

INSERT INTO public.users_roles (user_id, role_id)
VALUES ('d4574160-7e61-4838-a8ba-2dfcc31d0fce', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('d4574160-7e61-4838-a8ba-2dfcc31d0fce', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

-- CUSTOMERS
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('016f0990-ecfe-48a9-aba9-86b4e5488450', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('0d3bae50-38ca-46f6-bc85-12338d003d8a', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('1910e67b-d535-479b-81a9-b41028e4bdb6', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('3ee10e7d-6fef-4b39-89a2-b1f1137a0874', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));
INSERT INTO public.users_roles (user_id, role_id)
VALUES ('ced2a9b8-6e52-4e53-83fa-b13010b61e2f', (SELECT id FROM roles WHERE name = 'ROLE_CUSTOMER'));

