create extension if not exists pgcrypto;
create type public.mission_status as enum ('draft','published','paused','completed');
create type public.submission_status as enum ('pending','approved','rejected','needs_review');

create table public.profiles (
 id uuid primary key references auth.users(id) on delete cascade,
 username text unique not null check (char_length(username) between 3 and 24),
 display_name text not null default '',
 bio text not null default '' check (char_length(bio) <= 180),
 avatar_path text,
 home_city text not null default 'Coimbatore',
 points integer not null default 0 check (points >= 0),
 created_at timestamptz not null default now()
);
create table public.missions (
 id uuid primary key default gen_random_uuid(), slug text unique not null,
 title text not null check (char_length(title) between 4 and 90),
 summary text not null check (char_length(summary) between 12 and 320),
 instructions text not null default '', category text not null,
 difficulty text not null default 'easy' check (difficulty in ('easy','medium','bold')),
 estimated_minutes integer not null default 15 check (estimated_minutes between 1 and 240),
 points_reward integer not null default 20 check (points_reward between 0 and 500),
 city text not null, area text, latitude double precision, longitude double precision,
 cover_path text, status public.mission_status not null default 'draft',
 starts_at timestamptz, ends_at timestamptz, created_at timestamptz not null default now(),
 check ((latitude is null and longitude is null) or (latitude between -90 and 90 and longitude between -180 and 180))
);
create table public.submissions (
 id uuid primary key default gen_random_uuid(), mission_id uuid not null references public.missions(id) on delete cascade,
 user_id uuid not null references public.profiles(id) on delete cascade,
 caption text not null default '' check (char_length(caption) <= 500), media_path text not null,
 status public.submission_status not null default 'pending', submitted_at timestamptz not null default now(), reviewed_at timestamptz,
 unique (mission_id,user_id)
);
create table public.saved_missions (
 user_id uuid not null references public.profiles(id) on delete cascade,
 mission_id uuid not null references public.missions(id) on delete cascade,
 created_at timestamptz not null default now(), primary key(user_id,mission_id)
);
create table public.reports (
 id uuid primary key default gen_random_uuid(), reporter_id uuid not null references public.profiles(id) on delete cascade,
 submission_id uuid references public.submissions(id) on delete cascade,
 mission_id uuid references public.missions(id) on delete cascade,
 reason text not null check (char_length(reason) between 3 and 500), created_at timestamptz not null default now(),
 check (submission_id is not null or mission_id is not null)
);
create index missions_city_status_idx on public.missions(city,status);
create index submissions_user_date_idx on public.submissions(user_id,submitted_at desc);

alter table public.profiles enable row level security;
alter table public.missions enable row level security;
alter table public.submissions enable row level security;
alter table public.saved_missions enable row level security;
alter table public.reports enable row level security;
create policy "Profiles readable" on public.profiles for select using (true);
create policy "Create own profile" on public.profiles for insert to authenticated with check (auth.uid() = id);
create policy "Update own profile" on public.profiles for update to authenticated using (auth.uid() = id) with check (auth.uid() = id);
create policy "Published missions readable" on public.missions for select using (status = 'published');
create policy "Read own submissions" on public.submissions for select to authenticated using (auth.uid() = user_id);
create policy "Submit for self" on public.submissions for insert to authenticated with check (auth.uid() = user_id and status = 'pending');
create policy "Read own saves" on public.saved_missions for select to authenticated using (auth.uid() = user_id);
create policy "Save mission" on public.saved_missions for insert to authenticated with check (auth.uid() = user_id);
create policy "Unsave mission" on public.saved_missions for delete to authenticated using (auth.uid() = user_id);
create policy "Create reports" on public.reports for insert to authenticated with check (auth.uid() = reporter_id);
create policy "Read own reports" on public.reports for select to authenticated using (auth.uid() = reporter_id);

create or replace function public.handle_new_user() returns trigger language plpgsql security definer set search_path = public as $$
declare generated_username text;
begin
 generated_username := 'scene_' || substr(replace(new.id::text,'-',''),1,12);
 insert into public.profiles(id,username,display_name) values(new.id,generated_username,coalesce(new.raw_user_meta_data->>'display_name',''));
 return new;
end; $$;
create trigger on_auth_user_created after insert on auth.users for each row execute procedure public.handle_new_user();

insert into public.missions(slug,title,summary,instructions,category,difficulty,estimated_minutes,points_reward,city,area,status) values
('fifty-rupee-food-legend','Find the ₹50 food legend','Find a genuinely great local bite that costs ₹50 or less. Photograph the food and tell us why it is worth the stop.','Show the dish and its price. Be honest about the amount paid.','Food','easy',20,30,'Coimbatore','Gandhipuram','published'),
('shop-sign-comedy','The sign nobody notices','Find a funny, clever or wonderfully confusing shop sign hiding in plain sight.','Photograph from a public place. Do not include identifiable people without permission.','Street details','easy',10,20,'Coimbatore',null,'published'),
('quiet-corner','A quiet corner','Show a peaceful public place that most people walk past.','Choose a safe, publicly accessible spot. No trespassing or restricted property.','Hidden places','medium',25,35,'Coimbatore',null,'published'),
('neighbourhood-classic','A neighbourhood classic','Find a small local favourite and explain why the neighbourhood keeps coming back.','Ask before photographing staff or customers.','Food','easy',30,40,'Coimbatore',null,'published');
