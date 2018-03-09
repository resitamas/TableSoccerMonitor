package network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TableSoccerService {

	@GET("")
	Call<String> getData();

}
