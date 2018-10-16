import japgolly.scalajs.react.Callback

package object StreamUtils {
  /*
  def createUserStream(userId: String) = {
    return createStream("/api/chirps/live", function(stream) {
      stream.send(JSON.stringify({userIds: [userId]}));
    });
  }

  def createActivityStream(userId: String)= {
    return createStream("/api/activity/" + userId + "/live");
  }

  def createStream(path: String, onopen: Callback)= {
    return {
      connect: function(onChirp) {
        var stream = new WebSocket("ws://" + location.host + path);
        if (onopen) {
          stream.onopen = function(event) {
            onopen(stream, event);
          }.bind(this);
        }
        stream.onmessage = function(event) {
          var chirp = JSON.parse(event.data);
          onChirp(chirp);
        }.bind(this);
        return {
          close: function() {
            stream.close();
          }
        }
      }
    };
  }
*/

}
