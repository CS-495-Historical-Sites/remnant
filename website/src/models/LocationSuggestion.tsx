export default interface LocationSuggestion {
  id: number;
  user: number;
  suggestion_time: Date;
  name: string;
  latitude: number;
  longitude: number;
  short_description: number;
  wikidata_image_name: string;
  image_url: string
}

export default interface LocationEditSuggestion {
  id: number;
  location_id: number;
  user: number;
  suggestion_time: Date;
  name: string;
  short_description: number;
  long_description: string;
}
