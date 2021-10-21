package sinanakkoyun.minecraftinventorylog.itemstack;

import org.bukkit.inventory.ItemStack;

import java.io.*;

public class ItemStackUtil {
    public static ItemStack nullifyAir(ItemStack itemStack) {
        return itemStack.getType().isAir() ? null : itemStack;
    }
    public static boolean checkIfEqual(ItemStack[] contents1, ItemStack[] contents2) {
        int mismatch = 0;
        for(int i = 0; i < contents1.length; i++) {

            if(!nullifyAir(contents1[i]).equals(nullifyAir(contents2[i]))) {
                mismatch++;
            }
        }

        return !(mismatch > 0);
    }

    public static byte[] serializeContents(ItemStack[] object) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);

            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ex) {
            }
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }

    }

    /**
     * Converts a byte array to an Object.
     *
     * @param byteArray, a byte array that represents a serialized Object.
     * @return, an instance of the Object class.
     */
    public static ItemStack[] deserializeContents(byte[] byteArray) {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            ItemStack[] o = (ItemStack[]) in.readObject();
            return o;

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
            }
        }
    }
}
