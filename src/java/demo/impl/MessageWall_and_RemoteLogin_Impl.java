package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {

  private List<Message> messages;
  private Map<String, String> UserMap;

  public MessageWall_and_RemoteLogin_Impl() {
    messages = new ArrayList<Message>();
    UserMap = new HashMap<String, String>()
        {
            {
                put("Niklas", "Niklas");
                put("admin", "admin");
            }
        };
  }

  @Override
  public UserAccess connect(String usr, String passwd) {
      //UserMap<"Niklas"><"Niklas">
      if(UserMap.containsKey(usr)){
        String psw = UserMap.get(usr);

        if(psw.equals(passwd)){
            return new UserAccess_Impl(this, usr);
        }else{
            return null;
        }
      }else{
          return null;
      }
      // implements remote login
      // first exercise, always user is authanticated
      
      
  }

  @Override
  public void put(String user, String msg) {
      Message message = new Message_Impl(user, msg);
      messages.add(message);
  }

  @Override
  public boolean delete(String user, int index) {
      Message message = messages.get(index);
      if(message.getOwner() == null ? user == null : message.getOwner().equals(user)){
          messages.remove(index);
        return true;
      }
      else{
          return false;
      }
  }

  @Override
  public Message getLast() {
      if(messages.size() == 0){
          return new Message_Impl("user", "no message");
        
      } else{
          return messages.get(messages.size()-1);
      }
  }

  @Override
  public int getNumber() {
        return messages.size()-1;

  }

  @Override
  public List<Message> getAllMessages() {
    return messages;
  }

}
