create index conjuction_indexes using hash
on sn_otus.users_interests(user_id, interest_id);

create index interest_id_hash_index using hash
on sn_otus.interests(id);

create index users_id_hash_index using hash
on sn_otus.users(id);