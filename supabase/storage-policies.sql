-- Create a private bucket for user-submitted mission photos.
insert into storage.buckets (id,name,public,file_size_limit,allowed_mime_types)
values ('scene-submissions','scene-submissions',false,10485760,array['image/jpeg','image/png','image/webp'])
on conflict (id) do nothing;

create policy "SCENE users upload to own folder"
on storage.objects for insert to authenticated
with check (bucket_id = 'scene-submissions' and (storage.foldername(name))[1] = auth.uid()::text);

create policy "SCENE users read own uploads"
on storage.objects for select to authenticated
using (bucket_id = 'scene-submissions' and (storage.foldername(name))[1] = auth.uid()::text);
