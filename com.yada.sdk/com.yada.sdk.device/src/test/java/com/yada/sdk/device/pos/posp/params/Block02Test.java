package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.HexUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cuitao-pc on 16/6/22.
 */
public class Block02Test {
  private Block02 block;

  @Before
  public void before() {
    block = new Block02(HexUtil.parseHex("DF298184202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020080101000000200801010000003200100020202020202020202020202020202020202020202020202020202020202020202020202020202020DF25080220080101000000"));
  }

  @Test
  public void test() {
    System.out.println(block.paramVersion);
  }
}