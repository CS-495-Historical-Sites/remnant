export default interface LocationSuggestion {
  id: number;
  user: number;
  suggestion_time: Date;
  name: string;
  latitude: number;
  longitude: number;
  short_description: number;
  wikidata_image_name: string;
}
