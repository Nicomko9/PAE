package biz;

import biz.dto.JeDto;
import biz.dto.PresenceDto;
import java.util.List;

public interface PresenceUcc {

  PresenceDto register(PresenceDto presence);

  List<PresenceDto> register(List<PresenceDto> presencesDto);

  List<PresenceDto> getAllPresencesOf(JeDto jeDto);

  List<PresenceDto> getPresencesForContact(int id);

}
